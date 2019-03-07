package com.example.angel.networkingchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.angel.networkingchat.utilidades.Const;
import com.example.angel.networkingchat.utilidades.MyMessage;
import com.example.angel.networkingchat.utilidades.MyState;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_test);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                try {
                    Toast.makeText(getApplicationContext(), "Mensaje por enviar", Toast.LENGTH_LONG).show();
                    multicast("Angel", "Hola incredulo", MyState.PUBLIC_MSG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void multicast(
            String multicastMessage) throws IOException {
        multicast(multicastMessage.getBytes());
    }

    public void multicast(String usr, String msg, MyState state) throws IOException {
        byte[] buff = UtilFun.serialize(new MyMessage(usr, msg, state));
        System.out.println(buff);
        System.out.println(buff.length);
        multicast(buff);
    }

    public void multicast(byte[] buff) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName("230.0.0.0");
        DatagramPacket packet = new DatagramPacket(buff, buff.length, group, Const.PORT);
        socket.send(packet);
        socket.close();
    }

}
