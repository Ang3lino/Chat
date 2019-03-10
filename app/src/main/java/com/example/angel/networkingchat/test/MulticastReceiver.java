package com.example.angel.networkingchat.test;


import com.example.angel.networkingchat.utilidades.Const;
import com.example.angel.networkingchat.utilidades.Pack;
import com.example.angel.networkingchat.utilidades.UtilFun;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[Const.MAX_UDP_LENGTH];

    private int id;

    private MulticastReceiver() {
        super();
        id = 0;
    }

    private MulticastReceiver(int id) {
        super();
        this.id = id;
        System.out.println("Listener created with id: " + id);
    }

    public void run() {
        try {
            socket = new MulticastSocket(Const.PORT);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                Pack msg = (Pack) UtilFun.deserialize(packet.getData());
                System.out.printf("Thread %d got\n", id);
                System.out.println(msg);
                if (msg.getMessage().equals("exit")) {
                    break;
                }
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        MulticastReceiver[] receivers = new MulticastReceiver[5];
        System.out.println("Listening on: " + Const.IP);
        for (int i = 0; i < receivers.length; ++i) {
            receivers[i] = new MulticastReceiver(i);
            receivers[i].start();
        }
    }
}
