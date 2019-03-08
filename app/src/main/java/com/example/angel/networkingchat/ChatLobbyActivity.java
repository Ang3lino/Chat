package com.example.angel.networkingchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.os.Handler;

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
        new Thread(new OwnServer()).start();
    }

    private class OwnServer implements Runnable {
        private MulticastSocket socket;
        private byte[] buf = new byte[Const.MAX_UDP_LENGTH];

        // We cannot modify UI components directly if we are not the main thread
        // I'll use handler in this example
        private Handler handler = new Handler();

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
                            Toast.makeText(getApplicationContext(), msg.toString(), Toast.LENGTH_LONG).show();
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
