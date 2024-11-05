package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerInstance extends PacketSender {

    private boolean isActive = false;
    private DatagramSocket socket;

    public ServerInstance(InetAddress ip, int port, DatagramSocket socket) {
        super(socket, ip, port);
        this.socket=socket;
    }

    public boolean initConnection() {
        try {
            long ownID = 12345L;
            long serverID = 67890L;
            sendHello(ownID, serverID, "Hello, server!");

            System.out.println("HELLO message sent to server, awaiting ACK...");

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            Message receivedMessage = Message.fromBytes(receivePacket.getData());

            if (receivedMessage.isAck()) {
                System.out.println("Received ACK from server. Connection is now ACTIVE.");
                isActive = true;
            } else {
                System.out.println("Unexpected response from server: " + receivedMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
