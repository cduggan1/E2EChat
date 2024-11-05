package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender {

    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public PacketSender(DatagramSocket socket, InetAddress address, int port) {
        this.socket = socket;
        this.address = address;
        this.port = port;
    }

    public void sendHello(long senderId, long destinationId, String messageContent) throws IOException {
        Message helloMessage = new Message(Message.PacketType.HELLO, senderId, destinationId, messageContent);
        sendMessage(helloMessage);
    }

    public void sendAck(long senderId, long destinationId, String messageContent) throws IOException {
        Message ackMessage = new Message(Message.PacketType.ACK, senderId, destinationId, messageContent);
        sendMessage(ackMessage);
    }

    public void sendKeyExchange(long senderId, long destinationId, byte[] publicKey) throws IOException {
        Message keyExchangeMessage = new Message(Message.PacketType.KEY_EXCHANGE, senderId, destinationId, publicKey);
        sendMessage(keyExchangeMessage);
    }
    public void sendMessage(long senderId, long destinationId, String messageContent) throws IOException {
        Message messagePacket = new Message(Message.PacketType.MESSAGE, senderId, destinationId, messageContent);
        sendMessage(messagePacket);
    }

    void sendOnlineQuery(long senderId, long destinationId) throws IOException {
        Message messagePacket = new Message(Message.PacketType.ONLINE_QUERY, senderId,destinationId,"");
        sendMessage(messagePacket);
    }

    private void sendMessage(Message message) throws IOException {
        byte[] packetData = message.toBytes();
        DatagramPacket packet = new DatagramPacket(packetData, packetData.length, address, port);
        socket.send(packet);
    }
}
