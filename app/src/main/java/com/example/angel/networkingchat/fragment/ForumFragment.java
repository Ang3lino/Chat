package com.example.angel.networkingchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.text.emoji.widget.EmojiAppCompatEditText;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angel.networkingchat.ChatLobbyActivity;
import com.example.angel.networkingchat.R;
import com.example.angel.networkingchat.utilidades.MulticastPublisher;
import com.example.angel.networkingchat.utilidades.Pack;
import com.example.angel.networkingchat.utilidades.MyState;

import java.io.IOException;

// Do not use this new class if u wanna give support to every single android version
// import android.support.text.emoji.widget.EmojiEditText;

public class ForumFragment extends Fragment {
    EmojiAppCompatEditText txtToSend; // instance for giving support to old android version
    FloatingActionButton fabSend; // instance part of material design
    TextView txtPublicMsg;

    private void init(View view) {
        txtToSend = view.findViewById(R.id.txt_forum_msg_to_send);
        fabSend = view.findViewById(R.id.fab_send);
        txtPublicMsg = view.findViewById(R.id.txtview_public_msg);

        txtPublicMsg.setMovementMethod(new ScrollingMovementMethod());

        fabSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Pack pack = new Pack (
                            ChatLobbyActivity.sUsername,
                            txtToSend.getText().toString(),
                            MyState.PUBLIC_MSG
                    );
                    new MulticastPublisher(pack).start(); // don't forget to start the thread!
                    appendMessage(txtToSend.getText().toString());
                    txtToSend.setText(""); // clear the text
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void appendMessage(String str) {
        String current = txtPublicMsg.getText().toString();
        txtPublicMsg.setText(String.format("%s\n%s", current, str));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // we need to init EmojiText otherwise the app chashes. Start the config at this method as well
        EmojiCompat.Config config =
                new BundledEmojiCompatConfig(getContext());
        EmojiCompat.init(config);

        // you MUST use view in order to get references of the components
        View view =  inflater.inflate(R.layout.fragment_forum, container, false);
        init(view);
        return view;
    }

}
