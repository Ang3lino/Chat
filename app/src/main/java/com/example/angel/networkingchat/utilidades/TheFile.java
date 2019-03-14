package com.example.angel.networkingchat.utilidades;

public class TheFile {
    private String filename;
    private byte[] bytes;

    public TheFile(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    public String getFilename() { return filename; }

    public byte[] getBytes() { return bytes; }
}
