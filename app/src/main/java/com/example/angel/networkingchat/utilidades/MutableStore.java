package com.example.angel.networkingchat.utilidades;

import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableItem;

import java.util.ArrayList;

public class MutableStore {
    private static String sGlobalMessages;
    private static String sUsername;
    private static String sPrivateMessages;

    public static String getsPrivateMessages() { return sPrivateMessages; }
    public static void appendPrivateMessages(String msg) {
        sPrivateMessages += msg;
    }

    public static void setUsername(String username) { sUsername = username; }
    public static String getUsername() { return sUsername; }

    public static void appendGlobalMessages(String msg) {
        sGlobalMessages += msg;
    }

    public static String getGlobalMessages() { return sGlobalMessages; }

    private static ArrayList<PersonAvailableItem> availables;

    public static ArrayList<PersonAvailableItem> getAvailables() {
        if (availables == null) availables = new ArrayList<>();
        return availables;
    }

}
