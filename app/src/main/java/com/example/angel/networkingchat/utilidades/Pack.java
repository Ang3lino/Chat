package com.example.angel.networkingchat.utilidades;

import java.io.Serializable;

public class Pack implements Serializable {
    public static final long serialVersionUID = 1L;

    private String nickname, message;
    private MyState state;

    public Pack(String nick, String msg, MyState state) {
        nickname = nick;
        message = msg;
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format("State: %s\nNickname: %s\nMessage: %s", state, nickname, message);
    }

    public String getNickname() { return nickname; }
    public String getMessage() { return message; }
    public MyState getState() { return state; }
}