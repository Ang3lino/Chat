package com.example.angel.networkingchat.utilidades;

import java.io.File;
import java.io.Serializable;

public class Pack implements Serializable {
    public static final long serialVersionUID = 1L;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    private String nickname, message, receiver;
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
            for (byte aByte : bytes) builder.append(aByte + " ");
            return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s\nReceiver: %s\nBytes: %s\n",
                    state, nickname, message, file.toString(), receiver, builder.toString());
        }
        return  String.format("State: %s\nNickname: %s\nMessage: %s\nFile?: %s receiver: %s",
                state, nickname, message, "null", receiver);
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