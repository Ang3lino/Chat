package com.example.angel.networkingchat.utilidades;

public class MutableStore {
    private static String sGlobalMessages;

    public static void appendGlobalMessages(String msg) {
        sGlobalMessages += msg;
    }

    public static String getGlobalMessages() { return sGlobalMessages; }
}
