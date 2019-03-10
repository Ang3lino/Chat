package com.example.angel.networkingchat.utilidades;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher extends Thread {
    private byte[] buffer;

    public MulticastPublisher(byte[] buff) {
        buffer = buff;
    }

    // overloaded multicast functions
    public MulticastPublisher(Pack message) throws IOException {
        buffer = UtilFun.serialize(message);
    }

    public void multicast(String multicastMessage) throws IOException {
        multicast(multicastMessage.getBytes());
    }

    public void multicast(byte[] buff) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName("230.0.0.0");
        DatagramPacket packet = new DatagramPacket(buff, buff.length, group, Const.PORT);
        socket.send(packet);
        socket.close();
    }

    public void run() {
        try {
            multicast(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
