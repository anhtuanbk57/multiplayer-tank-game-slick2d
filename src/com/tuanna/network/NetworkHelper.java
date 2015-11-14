package com.tuanna.network;

import com.tuanna.main.Constants;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class NetworkHelper implements PacketReceiver.OnPackageReceivedListener {

    private static final byte OP_MOVE = 1;
    private static final byte OP_SHOOT = 2;
    private static final byte OP_DESTROY = 3;

    private InetAddress destinationAddress_;
    private int port_;
    private DataOutputStream outputStreamWrapper_;
    private ByteArrayOutputStream outputStream_;
    private PacketSender packetSender_;
    private PacketReceiver packetReceiver_;
    private NetworkMessageListener listener_;

    /**
     *
     * @param destinationAddress Address we will send packets to
     * @param port Destination port number
     */
    public NetworkHelper(InetAddress destinationAddress, int port) {
        destinationAddress_ = destinationAddress;
        port_ = port;
        outputStream_ = new ByteArrayOutputStream(256);
        outputStreamWrapper_ = new DataOutputStream(outputStream_);
        packetSender_ = new PacketSender();
        packetReceiver_ = new PacketReceiver();
    }

    public void setNetworkMessageListener(NetworkMessageListener listener) {
        packetReceiver_.setPackageReceivedListener(this);
        packetReceiver_.startReceivingOnPort(Constants.LOCAL_PORT);
        listener_ = listener;
    }

    public void removeNetworkMessageListener() {
        listener_ = null;
        packetReceiver_.removePackageReceivedListener();
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
            byte[] data = outputStream_.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, destinationAddress_, port_);
            packetSender_.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendShootMessage(int id, float x, float y, float rotation) {
        outputStream_.reset();
        try {
            outputStreamWrapper_.writeByte(OP_MOVE);
            outputStreamWrapper_.writeInt(id);
            outputStreamWrapper_.writeFloat(x);
            outputStreamWrapper_.writeFloat(y);
            outputStreamWrapper_.writeFloat(rotation);
            byte[] data = outputStream_.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, destinationAddress_, port_);
            packetSender_.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDestroyMessage(int id, float x, float y) {
        outputStream_.reset();
        try {
            outputStreamWrapper_.writeByte(OP_MOVE);
            outputStreamWrapper_.writeInt(id);
            outputStreamWrapper_.writeFloat(x);
            outputStreamWrapper_.writeFloat(y);
            byte[] data = outputStream_.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, destinationAddress_, port_);
            packetSender_.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
