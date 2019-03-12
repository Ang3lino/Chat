package com.example.angel.networkingchat.utilidades;

import com.example.angel.networkingchat.recyclerViewAvailablePeople.PersonAvailableItem;

import java.util.ArrayList;

public class MutableStore {
    private static String sGlobalMessages;

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
