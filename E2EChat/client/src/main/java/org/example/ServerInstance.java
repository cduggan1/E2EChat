package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerInstance extends PacketSender {

    private boolean isActive = false;
    private DatagramSocket socket;
    private long ownID;

    public ServerInstance(InetAddress ip, int port, DatagramSocket socket, long ownID) {
        super(socket, ip, port);
        this.socket = socket;
        this.ownID = ownID;
    }

    public boolean initConnection() {
        try {
            sendHello(ownID, 0L, "Hello from client " + ownID);
            System.out.println("HELLO message sent to server, awaiting ACK...");

            // Receive the acknowledgment with online users list
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            Message receivedMessage = Message.fromBytes(receivePacket.getData());

            if (receivedMessage.isAck()) {
                System.out.println("Received ACK from server: " + receivedMessage.getMessageContentAsString());
                isActive = true;

            } else {
                System.out.println("Unexpected response from server: " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isActive;
    }

    public List<Integer> onlineQuery() {
        if(!isActive){
            System.out.println("Server Instance Not Active.");
            return null;
        }
        try {
            sendOnlineQuery(ownID, 0L);
            System.out.println("ONLINEQUERY message sent to server, awaiting ACK...");

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            Message receivedMessage = Message.fromBytes(receivePacket.getData());

            if (receivedMessage.isAck()) {
                System.out.println("Received ONLINEQUERY from server: " + receivedMessage.getMessageContentAsString());
                return new ArrayList<>();
            } else {
                System.out.println("Unexpected response from server: " + receivedMessage);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isActive() {
        return isActive;
    }
}
