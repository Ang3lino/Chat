package com.example.angel.networkingchat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class ChatLobbyActivity extends AppCompatActivity {
    public static String sUsername;
    private static String TAG = "debug";
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
                startMaterialPicker(v);
            }
        });
    }

    private static final int REQUEST_WRITE_CODE = 2;

    private void startMaterialPicker(View v) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_CODE);
        }

        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(READ_REQUEST_CODE)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();

        // new MaterialFilePicker()
        //         .withActivity(this)
        //         .withRequestCode(READ_REQUEST_CODE)
        //         .withFilter(Pattern.compile(".*\\.png$")) // Filtering files and directories by file name using regexp
        //         .withFilterDirectories(true) // Set directories filterable (false by default)
        //         .withHiddenFiles(true) // Show hidden files and folders
        //         .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permisos dados", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permisos NO dados", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this, path, Toast.LENGTH_LONG).show();
            sendFile(new File(path));
        }
    }

    private void sendFile(File file) {
        Pack pack = new Pack(MyState.FILE_SENT);
        pack.setFile(file);
        byte[] bytes = UtilFun.fileToBytes(file);
        if (bytes.length > Const.MAX_UDP_LENGTH) {
            Toast.makeText(getApplicationContext(), "" + bytes.length,
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Archivo muy grande, no se enviara.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        pack.setBytes(bytes);
        try {
            new MulticastPublisher(pack).start();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void handleFileSent(final Pack p) {
        byte[] source = p.getBytes();
        String filename = p.getFile().getName();

        File file = new File (getFilesDir(), filename);
        try {
            // The file is only visible by the app
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(source);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),
                String.format("Archivo %s guardado en %s", file.toString(), file.getAbsolutePath()),
                Toast.LENGTH_LONG).show();
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
                            MyState currentState = pack.getState();
                            switch (currentState) {
                                case PUBLIC_MSG: handlePublicMessage(pack); break;
                                case LOG_IN: handleLogIn(pack); break;
                                case FILE_SENT: handleFileSent(pack);
                            }
                            //if (currentState == MyState.FILE_SENT) handleFileSent(pack);
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
