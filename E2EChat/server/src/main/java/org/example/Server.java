package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server started and listening on port " + PORT);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                Message receivedMessage = Message.fromBytes(receivePacket.getData());
                System.out.println("Received message from client: " + receivedMessage);

                if (receivedMessage.isHello()) {
                    System.out.println("Sending ACK message to client.");

                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();
                    PacketSender packetSender = new PacketSender(serverSocket, clientAddress, clientPort);

                    packetSender.sendAck(receivedMessage.getDestinationId(), receivedMessage.getSenderId(), "Acknowledged HELLO message.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
