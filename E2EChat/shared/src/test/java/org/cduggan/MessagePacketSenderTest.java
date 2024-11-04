package org.cduggan;

import org.example.Message;
import org.example.PacketSender;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagePacketSenderTest {

    @Test
    public void testHelloMessage() throws IOException {
        testPacketTransmission(Message.PacketType.HELLO, 12345L, 67890L, "Hello, World!");
    }

    @Test
    public void testAckMessage() throws IOException {
        testPacketTransmission(Message.PacketType.ACK, 12345L, 67890L, "Acknowledgement");
    }

    @Test
    public void testMessagePacket() throws IOException {
        testPacketTransmission(Message.PacketType.MESSAGE, 12345L, 67890L, "This is a test message.");
    }

    @Test
    public void testErrorMessage() throws IOException {
        testPacketTransmission(Message.PacketType.ERROR, 12345L, 67890L, "An error occurred.");
    }

    private void testPacketTransmission(Message.PacketType type, long senderId, long destinationId, String content) throws IOException {
        Message originalMessage = new Message(type, senderId, destinationId, content);

        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        PacketSender packetSender = new PacketSender(byteOutStream);
        packetSender.sendMessage(originalMessage);

        byte[] packetBytes = byteOutStream.toByteArray();
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(packetBytes);

        byte[] receivedBytes = new byte[packetBytes.length];
        int bytesRead = byteInStream.read(receivedBytes);

        assertEquals(packetBytes.length, bytesRead, "Mismatch in the number of bytes read");

        Message reconstructedMessage = Message.fromBytes(receivedBytes);

        assertEquals(originalMessage.toString(), reconstructedMessage.toString(), "The original and reconstructed messages should be identical");
    }
}