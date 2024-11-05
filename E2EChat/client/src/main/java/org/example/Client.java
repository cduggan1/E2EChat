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

            ServerInstance si = new ServerInstance(serverAddress,SERVER_PORT,clientSocket);

            si.initConnection();

            // Generate key pair for the client
//            KeyPairGeneratorUtil keyGenUtil = new KeyPairGeneratorUtil();
//            byte[] publicKeyBytes = keyGenUtil.getPublicKey().getEncoded();
//
//            // Send the public key to the server as a KEY_EXCHANGE message
//            PacketSender packetSender = new PacketSender(clientSocket, serverAddress, SERVER_PORT);
//            packetSender.sendKeyExchange(12345L, 67890L, publicKeyBytes);
//
//            System.out.println("Public key sent to server.");
//
//            // Prepare buffer to receive the acknowledgment
//            byte[] buffer = new byte[1024];
//            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
//
//            // Receive the server's response
//            clientSocket.receive(receivePacket);
//
//            // Parse the received message
//            Message receivedMessage = Message.fromBytes(receivePacket.getData());
//
//            // Check if the response is an ACK and print it
//            if (receivedMessage.isAck()) {
//                System.out.println("Received ACK from server: " + receivedMessage.getMessageContentAsString());
//            } else {
//                System.out.println("Unexpected response from server: " + receivedMessage);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
