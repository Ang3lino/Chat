package com.example.angel.networkingchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.angel.networkingchat.R;
import com.example.angel.networkingchat.utilidades.MutableStore;

public class PrivateMessageFragment extends Fragment {
    TextView txtPrivateMsg;

    public void appendMessage(String str) {
        String current = txtPrivateMsg.getText().toString();
        txtPrivateMsg.setText(String.format("%s\n%s", current, str));
    }

    public void init(View view) {
        txtPrivateMsg = view.findViewById(R.id.txtview_private_msg);

        txtPrivateMsg.setMovementMethod(new ScrollingMovementMethod());
        String privateMessages = MutableStore.getsPrivateMessages();
        if (privateMessages != null)
            txtPrivateMsg.setText(privateMessages);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // you MUST use view in order to get references of the components
        View view =  inflater.inflate(R.layout.fragment_private_message, container, false);
        init(view);
        return view;
    }
}
