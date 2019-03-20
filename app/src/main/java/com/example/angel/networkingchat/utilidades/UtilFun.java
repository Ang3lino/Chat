package com.example.angel.networkingchat.utilidades;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.*;
import java.net.URISyntaxException;

public class UtilFun {

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static boolean writeBytes(byte[] src, String filepath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            fos.write(src);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static byte[] fileToBytes(File file) {
        int len = (int) file.length();
        byte[] bytes = new byte[len];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    /**
     * You should cast the object returned.
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
