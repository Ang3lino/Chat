package com.example.angel.networkingchat.utilidades;

import java.io.File;
import java.io.Serializable;

public class Pack implements Serializable {
    public static final long serialVersionUID = 1L;

    private String nickname, message;
    private MyState state;
    private File file; // this may be null
    private byte[] bytes;

    public Pack(String nick, String msg, MyState state) {
        nickname = nick;
        message = msg;
        this.state = state;
        file = null;
    }

    public Pack(MyState state) {
        this.state = state;
    }

    public Pack(String nick, String msg, MyState state, File file) {
        this(nick, msg, state);
        this.file = file;
    }

    @Override
    public String toString() {
        if (file != null) {
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) builder.append(aByte);
            return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s\nBytes: %s",
                    state, nickname, message, file.toString(), builder.toString());
        }
        return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s",
                state, nickname, message, "null");
    }

    public void setFile(File file) { this.file = file; }
    public void setBytes(byte[] bytes) { this.bytes = bytes; }
    public void setState(MyState state) { this.state = state; }

    public File getFile() { return file; }
    public byte[] getBytes() { return bytes; }
    public String getNickname() { return nickname; }
    public String getMessage() { return message; }
    public MyState getState() { return state; }
}