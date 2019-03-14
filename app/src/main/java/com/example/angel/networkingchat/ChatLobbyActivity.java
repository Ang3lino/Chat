package com.example.angel.networkingchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import com.example.angel.networkingchat.fragment.ActivePeopleFragment;
import com.example.angel.networkingchat.fragment.ForumFragment;
import com.example.angel.networkingchat.fragment.PrivateMessageFragment;
import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableItem;
import com.example.angel.networkingchat.utilidades.Const;
import com.example.angel.networkingchat.utilidades.MulticastPublisher;
import com.example.angel.networkingchat.utilidades.MutableStore;
import com.example.angel.networkingchat.utilidades.MyState;
import com.example.angel.networkingchat.utilidades.Pack;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ChatLobbyActivity extends AppCompatActivity {
    public static String sUsername;
    private static final int READ_REQUEST_CODE = 42;

    FloatingActionButton mFabSendFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lobby);
        init();
    }

    public void init() {
        // start the server
        new Thread(new OwnServer(getApplicationContext())).start();

        Intent intent = getIntent();
        sUsername = intent.getStringExtra(MainActivity.USERNAME);
        //Toast.makeText(this, sUsername, Toast.LENGTH_SHORT).show();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        replaceFragment(R.id.fragment_container, new ForumFragment());

        mFabSendFile = findViewById(R.id.fab_send_file);
        mFabSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile(v);
            }
        });
    }

    private void sendFile(View v) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            Toast.makeText(this, "No tiene la version de android minima requerida, no se "
                    + "puede enviar el archivo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // To search for all documents available
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                File file = new File(uri.getPath());
                Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
                try {
                    byte[] bytes = UtilFun.serialize(
                            new Pack(sUsername, "", MyState.FILE_SENT, file));
                    if (bytes.length > Const.MAX_UDP_LENGTH) {
                        Toast.makeText(this, "El archivo mide mas de "
                                + Const.MAX_UDP_LENGTH + " bytes, no se envio.", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    new MulticastPublisher(bytes).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void replaceFragment(int containerID, Fragment fragmentSelected) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerID, fragmentSelected)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragmentSelected; // null at first

                    switch (menuItem.getItemId()) {
                        case R.id.nav_private_chat:
                            fragmentSelected = new PrivateMessageFragment(); break;
                        case R.id.nav_forum:
                            fragmentSelected = new ForumFragment(); break;
                        case R.id.nav_people_available:
                            fragmentSelected = new ActivePeopleFragment(); break;

                        // avoid the posibility of being null
                        default: fragmentSelected = new ForumFragment(); break;
                    }

                    replaceFragment(R.id.fragment_container, fragmentSelected);

                    return true;
                }
            };

    private void handlePublicMessage(Pack p) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        String msgToAdd = String.format("%s: %s", p.getNickname(), p.getMessage());
        if (fragment instanceof ForumFragment) {
            // do something with the fragment
            ((ForumFragment) fragment).appendMessage(msgToAdd);
        }
        MutableStore.appendGlobalMessages(msgToAdd + "\n");
    }

    private void handleLogIn(Pack p) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        PersonAvailableItem person =
                new PersonAvailableItem(R.drawable.ic_tag_faces_black_24dp, p.getNickname(), "Address");
        // if the user is watching this fragment add data and notify to the adapter
        if (fragment instanceof ActivePeopleFragment) {
            ((ActivePeopleFragment) fragment).addActivePerson(person);

        } else { // just add the person to the arrayList, when the user look this fragment will load it
            MutableStore.getAvailables().add(person);
        }
    }

    // Nested class, it does the role of controller as well
    public class OwnServer implements Runnable {
        private MulticastSocket socket;
        private byte[] buf = new byte[Const.MAX_UDP_LENGTH];
        private Context context;

        // We cannot modify UI components directly if we are not the main thread
        // I'll use handler in this example
        private Handler handler = new Handler();

        public OwnServer(Context context) {
            this.context = context;
        }

        private void initSocket() {
            try {
                socket = new MulticastSocket(Const.PORT); // it may throw IOException
                InetAddress group = null;
                try {
                    group = InetAddress.getByName("230.0.0.0");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                socket.joinGroup(group); // it may throw IOException
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            initSocket();
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    final Pack pack = (Pack) UtilFun.deserialize(packet.getData());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, pack.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyState currentState = pack.getState();
                            if (currentState == MyState.PUBLIC_MSG) handlePublicMessage(pack);
                            if (currentState == MyState.LOG_IN) handleLogIn(pack);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        // unreachable statements
        // socket.leaveGroup(group);
        // socket.close();
    }


}
