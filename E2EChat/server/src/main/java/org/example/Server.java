package org.example;

import org.example.Message;
import org.example.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Server {

    private static final int PORT = 12345;
    private final HashMap<Long, ClientInfo> activeClients = new HashMap<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("Server started and listening on port " + PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                Message receivedMessage = Message.fromBytes(receivePacket.getData());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                handleMessage(receivedMessage, clientAddress, clientPort, serverSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message message, InetAddress clientAddress, int clientPort, DatagramSocket socket) throws IOException {
        PacketSender packetSender = new PacketSender(socket, clientAddress, clientPort);
        long userId = message.getSenderId();

        if (message.isHello()) {
            activeClients.put(userId, new ClientInfo(clientAddress, clientPort));
            System.out.println("Stored new client with userID: " + userId);

            String ackMessage = "Hello acknowledged, client registered.";
            packetSender.sendAck(0L, userId, ackMessage);
        }else if(message.isOnlineQuery()){
            String onlineUsers = activeClients.keySet().stream()
                    .filter(id -> id != userId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            packetSender.sendAck(0L,userId,onlineUsers);
        }
        else if (message.isMessage()) {
            long destinationId = message.getDestinationId();
            ClientInfo recipient = activeClients.get(destinationId);
            if (recipient != null) {
                PacketSender forwardSender = new PacketSender(socket, recipient.address, recipient.port);
                forwardSender.sendMessage(message.getSenderId(), destinationId, message.getMessageContentAsString());
                System.out.println("Message forwarded to user ID: " + destinationId);
            } else {
                System.out.println("Destination user ID not found: " + destinationId);
                packetSender.sendMessage(0L, message.getSenderId(), "Error: User not found.");
            }
        }
    }

    private static class ClientInfo {
        InetAddress address;
        int port;

        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }
}
