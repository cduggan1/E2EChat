package org.example;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {

    public enum PacketType {
        HELLO((byte) 1),
        ACK((byte) 2),
        MESSAGE((byte) 3),
        ERROR((byte) 4),
        KEY_EXCHANGE((byte) 5),
        ONLINE_QUERY((byte) 6);

        private final byte type;

        PacketType(byte type) {
            this.type = type;
        }

        public byte getType() {
            return type;
        }

        public static PacketType fromByte(byte value) {
            for (PacketType packetType : PacketType.values()) {
                if (packetType.getType() == value) {
                    return packetType;
                }
            }
            return null;
        }
    }

    private final PacketType type;
    private final long senderId;
    private final long destinationId;
    private final byte[] messageContent;

    public Message(PacketType type, long senderId, long destinationId, String messageContent) {
        this.type = type;
        this.senderId = senderId;
        this.destinationId = destinationId;
        this.messageContent = messageContent.getBytes(StandardCharsets.UTF_8);
    }

    public Message(PacketType type, long senderId, long destinationId, byte[] messageContent) {
        this.type = type;
        this.senderId = senderId;
        this.destinationId = destinationId;
        this.messageContent = messageContent;
    }

    public PacketType getType() {
        return type;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public boolean isHello() {
        return this.type == PacketType.HELLO;
    }

    public boolean isAck() {
        return this.type == PacketType.ACK;
    }

    public boolean isMessage() {
        return this.type == PacketType.MESSAGE;
    }

    public boolean isError() {
        return this.type == PacketType.ERROR;
    }

    public boolean isKeyExchange() {
        return this.type == PacketType.KEY_EXCHANGE;
    }

    public boolean isOnlineQuery(){
        return this.type == PacketType.ONLINE_QUERY;
    }

    public String getMessageContentAsString() {
        return new String(messageContent, StandardCharsets.UTF_8);
    }

    public byte[] getMessageContentAsBytes() {
        return messageContent;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1 + 8 + 8 + 4 + messageContent.length);
        buffer.put(type.getType());
        buffer.putLong(senderId);
        buffer.putLong(destinationId);
        buffer.putInt(messageContent.length);
        buffer.put(messageContent);
        return buffer.array();
    }

    public static Message fromBytes(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet);
        byte typeByte = buffer.get();
        PacketType type = PacketType.fromByte(typeByte);
        long senderId = buffer.getLong();
        long destinationId = buffer.getLong();
        int length = buffer.getInt();
        byte[] messageBytes = new byte[length];
        buffer.get(messageBytes);
        return new Message(type, senderId, destinationId, messageBytes);
    }

    @Override
    public String toString() {
        if (type == PacketType.KEY_EXCHANGE) {
            return String.format("Type: %s, Sender ID: %016X, Destination ID: %016X, Message: [Public Key]",
                    type.name(), senderId, destinationId);
        } else {
            return String.format("Type: %s, Sender ID: %016X, Destination ID: %016X, Message: %s",
                    type.name(), senderId, destinationId, getMessageContentAsString());
        }
    }
}
