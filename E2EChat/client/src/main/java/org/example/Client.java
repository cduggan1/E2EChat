package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your user ID: ");
        long clientUserId = scanner.nextLong();
        scanner.nextLine();

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            ServerInstance si = new ServerInstance(serverAddress, SERVER_PORT, clientSocket, clientUserId);

            if (si.initConnection()) {
                System.out.println("Client connected successfully.");

                si.onlineQuery();

                Thread listenerThread = new Thread(() -> listenForMessages(clientSocket));
                listenerThread.start();

                while (true) {
                    System.out.print("Enter destination user ID (or 'exit' to quit): ");
                    String destinationInput = scanner.nextLine();

                    if (destinationInput.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting...");
                        break;
                    }

                    long destinationUserId;
                    try {
                        destinationUserId = Long.parseLong(destinationInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid user ID. Please enter a valid number.");
                        continue;
                    }

                    System.out.print("Enter message to send: ");
                    String messageContent = scanner.nextLine();

                    si.sendMessage(clientUserId, destinationUserId, messageContent);
                    System.out.println("Message sent to user " + destinationUserId);
                }

                listenerThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void listenForMessages(DatagramSocket clientSocket) {
        byte[] buffer = new byte[1024];
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(receivePacket);

                Message receivedMessage = Message.fromBytes(receivePacket.getData());
                System.out.println("\nReceived message from user " + receivedMessage.getSenderId() + ": "
                        + receivedMessage.getMessageContentAsString());
            } catch (Exception e) {
                if (!clientSocket.isClosed()) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
