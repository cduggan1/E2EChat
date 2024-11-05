package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.HashSet;

public class Server {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        HashSet<Integer> activeUserID = new HashSet<>();
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server started and listening on port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                Message receivedMessage = Message.fromBytes(receivePacket.getData());
                System.out.println("Received message from client: " + receivedMessage);
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                PacketSender packetSender = new PacketSender(serverSocket, clientAddress, clientPort);

                handleMessage(receivedMessage, packetSender);
//                if (receivedMessage.isKeyExchange()) {
//                    byte[] publicKeyBytes = receivedMessage.getMessageContentAsBytes();
//
//                    PublicKey clientPublicKey = KeyUtil.convertToPublicKey(publicKeyBytes);
//                    System.out.println("Received and stored public key from client: " + clientPublicKey);
//
//                    packetSender.sendAck(receivedMessage.getDestinationId(), receivedMessage.getSenderId(), "Public key received and stored.");
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleMessage(Message m, PacketSender ps) throws IOException {
        if(m.isHello()){
            System.out.println("Received Hello");
            ps.sendAck(1L,2L, "Hello");
        }
    }
}
