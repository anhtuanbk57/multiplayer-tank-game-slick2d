package com.tuanna.network;

import com.tuanna.main.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PacketSender {

    private DatagramSocket datagramSocket_;

    public PacketSender() {
        try {
            datagramSocket_ = new DatagramSocket(Constants.SEND_PORT);
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
