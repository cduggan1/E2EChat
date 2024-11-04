package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);

            PacketSender packetSender = new PacketSender(clientSocket, serverAddress, SERVER_PORT);

            packetSender.sendHello(12345L, 67890L, "Hello, server!");
            System.out.println("HELLO message sent to server.");

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            clientSocket.receive(receivePacket);
            Message receivedMessage = Message.fromBytes(receivePacket.getData());

            System.out.println("Received message from server: " + receivedMessage);

            if (receivedMessage.isAck()) {
                System.out.println("Received ACK from server.");
            } else {
                System.out.println("Unexpected response from server.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
