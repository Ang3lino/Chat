package com.example.angel.networkingchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.networkingchat.utilidades.MulticastPublisher;
import com.example.angel.networkingchat.utilidades.MyMessage;
import com.example.angel.networkingchat.utilidades.MyState;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView txtUsrname;

    public static final String USERNAME = "com.example.angel.networkingchat.USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null){
            WifiManager.MulticastLock lock = wifi.createMulticastLock("HelloAndroid");
            lock.acquire();
        }

        findViews();

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                onClickLogin(v);
            }
        });

    }

    public void findViews() {
        btn = findViewById(R.id.btn_login);
        txtUsrname = findViewById(R.id.txt_nick);
    }

    public void onClickLogin(View v) {
        Toast.makeText(getApplicationContext(), "Mensaje por enviar", Toast.LENGTH_SHORT).show();
        try {
            new MulticastPublisher( // extends a thread
                UtilFun.serialize (
                    new MyMessage("Angel", "Finalmente quedo esto", MyState.PUBLIC_MSG)
                )
            ).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // it may not be received or catched by the server
        Toast.makeText(getApplicationContext(), "Mesaje enviado", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChatLobbyActivity.class);
        intent.putExtra(USERNAME, txtUsrname.getText().toString());
        startActivity(intent);
    }


}
