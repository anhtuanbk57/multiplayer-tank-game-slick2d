package com.tuanna.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PacketReceiver {

    private Thread receiverThread_;
    private DatagramSocket datagramSocket_;
    private OnPackageReceivedListener listener_;
    private boolean shouldContinueReceiving_;

    public PacketReceiver() {
        receiverThread_ = new Thread() {
            byte[] buf = new byte[256];

            @Override
            public void run() {
                while (shouldContinueReceiving_) {
                    if (datagramSocket_ != null) {
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        try {
                            datagramSocket_.receive(packet);
                            if (listener_ != null) {
                                listener_.onPackageReceived(packet);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    public void setPackageReceivedListener(OnPackageReceivedListener listener) {
        listener_ = listener;
    }

    public void removePackageReceivedListener() {
        listener_ = null;
        stopReceiving();
    }

    public void startReceivingOnPort(int port) {
        try {
            datagramSocket_ = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        shouldContinueReceiving_ = true;
        if (!receiverThread_.isAlive()) {
            receiverThread_.start();
        }
    }

    public void stopReceiving() {
        shouldContinueReceiving_ = false;
    }

    public interface OnPackageReceivedListener {
        void onPackageReceived(DatagramPacket packet);
    }
}
