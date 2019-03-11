package com.example.angel.networkingchat;

import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.widget.EmojiAppCompatEditText;
import android.widget.TextView;

import com.example.angel.networkingchat.fragment.ForumFragment;

public class ForumController {
    EmojiAppCompatEditText txtToSend; // instance for giving support to old android version
    FloatingActionButton fabSend; // instance part of material design
    TextView txtPublicMsg;

    void appendText(String str) {
        String current = txtPublicMsg.getText().toString();
        txtPublicMsg.setText(String.format("%s\n%s", current, str));
    }

    public ForumController() {

    }
}
