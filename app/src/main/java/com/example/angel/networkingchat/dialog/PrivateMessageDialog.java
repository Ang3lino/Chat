package com.example.angel.networkingchat.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.angel.networkingchat.MainActivity;
import com.example.angel.networkingchat.R;
import com.example.angel.networkingchat.fragment.ActivePeopleFragment;
import com.example.angel.networkingchat.utilidades.MulticastPublisher;
import com.example.angel.networkingchat.utilidades.MutableStore;
import com.example.angel.networkingchat.utilidades.MyState;
import com.example.angel.networkingchat.utilidades.Pack;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.IOException;

public class PrivateMessageDialog extends AppCompatDialogFragment {
    private EditText txtMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_private_message, null);

        builder.setView(view)
                .setTitle("Mensaje privado")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // do nothing if pressed
                    }
                })
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPrivateMessage();
                    }
                });

        txtMessage = view.findViewById(R.id.txt_private_msg);

        return builder.create();
    }

    public void sendPrivateMessage() {
        String nick = MutableStore.getUsername();
        String receiver = getArguments().getString(ActivePeopleFragment.RECEIVER);
        String msg = txtMessage.getText().toString();

        Pack pack = new Pack(MyState.PRIVATE_MSG);
        pack.setNickname(nick);
        pack.setReceiver(receiver);
        pack.setMessage(msg);

        try {
            new MulticastPublisher(UtilFun.serialize(pack)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
