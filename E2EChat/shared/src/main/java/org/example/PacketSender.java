package org.example;

import java.io.IOException;
import java.io.OutputStream;

public class PacketSender {

    private final OutputStream outputStream;

    public PacketSender(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendHello(long senderId, long destinationId, String messageContent) throws IOException {
        Message helloMessage = new Message(Message.PacketType.HELLO, senderId, destinationId, messageContent);
        sendMessage(helloMessage);
    }

    public void sendAck(long senderId, long destinationId, String messageContent) throws IOException {
        Message ackMessage = new Message(Message.PacketType.ACK, senderId, destinationId, messageContent);
        sendMessage(ackMessage);
    }

    public void sendMessage(long senderId, long destinationId, String messageContent) throws IOException {
        Message messagePacket = new Message(Message.PacketType.MESSAGE, senderId, destinationId, messageContent);
        sendMessage(messagePacket);
    }

    public void sendError(long senderId, long destinationId, String messageContent) throws IOException {
        Message errorMessage = new Message(Message.PacketType.ERROR, senderId, destinationId, messageContent);
        sendMessage(errorMessage);
    }

    public void sendMessage(Message message) throws IOException {
        byte[] packet = message.toBytes();
        outputStream.write(packet);
        outputStream.flush();
    }
}
