package com.example.angel.networkingchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.Handler;

import com.example.angel.networkingchat.fragment.ActivePeopleFragment;
import com.example.angel.networkingchat.fragment.ForumFragment;
import com.example.angel.networkingchat.fragment.PrivateMessageFragment;
import com.example.angel.networkingchat.utilidades.Const;
import com.example.angel.networkingchat.utilidades.MyMessage;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChatLobbyActivity extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lobby);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USERNAME);
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        new Thread(new OwnServer(getApplicationContext())).start();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragmentSelected = null;

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

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragmentSelected)
                            .commit();

                    return true;
                }
            };

    // Nested class ============================
    private class OwnServer implements Runnable {
        private MulticastSocket socket;
        private byte[] buf = new byte[Const.MAX_UDP_LENGTH];
        private Context context;

        // We cannot modify UI components directly if we are not the main thread
        // I'll use handler in this example
        private Handler handler = new Handler();

        public OwnServer(Context context) {
            this.context = context;
        }

        public void run() {
            try {
                socket = new MulticastSocket(Const.PORT);
                InetAddress group = InetAddress.getByName("230.0.0.0");
                socket.joinGroup(group);
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    final MyMessage msg = (MyMessage) UtilFun.deserialize(packet.getData());
                    System.out.println(msg);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, msg.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
                // unreachable statements
                // socket.leaveGroup(group);
                // socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
