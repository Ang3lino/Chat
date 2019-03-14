package com.example.angel.networkingchat.utilidades;

import java.io.File;
import java.io.Serializable;

public class Pack implements Serializable {
    public static final long serialVersionUID = 1L;

    private String nickname, message;
    private MyState state;
    private File file; // this may be null

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public Pack(String nick, String msg, MyState state) {
        nickname = nick;
        message = msg;
        this.state = state;
        file = null;
    }

    public Pack(String nick, String msg, MyState state, File file) {
        this(nick, msg, state);
        this.file = file;
    }

    @Override
    public String toString() {
        if (file != null) return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s",
                state, nickname, message, file.toString());
        return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s",
                state, nickname, message, "null");
    }

    public String getNickname() { return nickname; }
    public String getMessage() { return message; }
    public MyState getState() { return state; }
}