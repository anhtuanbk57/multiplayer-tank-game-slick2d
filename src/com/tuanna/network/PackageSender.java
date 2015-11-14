package com.tuanna.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PackageSender {

    private DatagramSocket datagramSocket_;

    public PackageSender() {
        try {
            datagramSocket_ = new DatagramSocket(6999);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(DatagramPacket packet) {
        try {
            datagramSocket_.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
