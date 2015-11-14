package com.tuanna.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PackageReceiver {

    private Thread receiverThread_;
    private DatagramSocket datagramSocket_;
    private OnPackageReceivedListener listener_;

    public PackageReceiver() {
        receiverThread_ = new Thread() {
            @Override
            public void run() {
                if (datagramSocket_ != null) {
                    byte[] buf = new byte[256];
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
        };
    }

    public void setPackageReceivedListener(OnPackageReceivedListener listener) {
        listener_ = listener;
    }

    public void startReceivingOnPort(int port) {
        try {
            if (datagramSocket_ != null) {
                datagramSocket_.close();
            }
            datagramSocket_ = new DatagramSocket(9996);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (!receiverThread_.isAlive()) {
            receiverThread_.start();
        }
    }

    public interface OnPackageReceivedListener {
        void onPackageReceived(DatagramPacket packet);
    }
}
