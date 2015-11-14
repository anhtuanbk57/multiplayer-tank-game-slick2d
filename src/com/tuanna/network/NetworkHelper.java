package com.tuanna.network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class NetworkHelper implements PackageReceiver.OnPackageReceivedListener {

    private static final byte OP_MOVE = 1;
    private static final byte OP_SHOOT = 2;
    private static final byte OP_DESTROY = 3;

    private InetAddress inetAddress_;
    private int port_;
    private DataOutputStream outputStreamWrapper_;
    private ByteArrayOutputStream outputStream_;
    private PackageSender packageSender_;
    private PackageReceiver packageReceiver_;
    private NetworkMessageListener listener_;

    public NetworkHelper(InetAddress inetAddress, int port) {
        inetAddress_ = inetAddress;
        port_ = port;
        outputStream_ = new ByteArrayOutputStream(256);
        outputStreamWrapper_ = new DataOutputStream(outputStream_);
        packageSender_ = new PackageSender();
        packageReceiver_ = new PackageReceiver();
        packageReceiver_.setPackageReceivedListener(this);
    }

    public void setNetworkMessageListener(NetworkMessageListener listener) {
        listener_ = listener;
    }

    public void sendMoveMessage(int id, float x, float y, float rotation, float velocity) {
        outputStream_.reset();
        try {
            outputStreamWrapper_.writeByte(OP_MOVE);
            outputStreamWrapper_.writeInt(id);
            outputStreamWrapper_.writeFloat(x);
            outputStreamWrapper_.writeFloat(y);
            outputStreamWrapper_.writeFloat(rotation);
            outputStreamWrapper_.writeFloat(velocity);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendShootMessage(int id, float x, float y, float rotation) {

    }

    public void sendDestroyMessage(int id, float x, float y) {

    }

    @Override
    public void onPackageReceived(DatagramPacket packet) {
        if (listener_ == null) {
            return;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getData());
        DataInputStream inputStreamWrapper = new DataInputStream(inputStream);
        try {
            int opcode = inputStreamWrapper.readByte();
            switch (opcode) {
                case OP_MOVE:
                    handleMoveMessage(inputStreamWrapper);
                    break;
                case OP_SHOOT:
                    handleShootMessage(inputStreamWrapper);
                    break;
                case OP_DESTROY:
                    handleDestroyMessage(inputStreamWrapper);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                inputStreamWrapper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMoveMessage(DataInputStream inputStream) {
        try {
            int id = inputStream.readInt();
            float x = inputStream.readFloat();
            float y = inputStream.readFloat();
            float rotation = inputStream.readFloat();
            float velocity = inputStream.readFloat();
            // Below statement assume that listener_ is not null, because this method
            // was called from onPackageReceived()
            listener_.onMoveMessageReceived(id, x, y, rotation, velocity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleShootMessage(DataInputStream inputStream) {
        try {
            int id = inputStream.readInt();
            float x = inputStream.readFloat();
            float y = inputStream.readFloat();
            float rotation = inputStream.readFloat();
            // Below statement assume that listener_ is not null, because this method
            // was called from onPackageReceived()
            listener_.onShootMessageReceived(id, x, y, rotation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDestroyMessage(DataInputStream inputStream) {
        try {
            int id = inputStream.readInt();
            float x = inputStream.readFloat();
            float y = inputStream.readFloat();
            // Below statement assume that listener_ is not null, because this method
            // was called from onPackageReceived()
            listener_.onDestroyMessageReceived(id, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface NetworkMessageListener {
        void onMoveMessageReceived(int id, float x, float y, float rotation, float velocity);
        void onShootMessageReceived(int id, float x, float y, float rotation);
        void onDestroyMessageReceived(int id, float x, float y);
    }
}
