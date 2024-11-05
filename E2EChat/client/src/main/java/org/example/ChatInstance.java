package org.example;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class ChatInstance extends PacketSender {

    private final long userID;
    private final long ownID;
    private PublicKey peerPublicKey;
    private PrivateKey ownPrivateKey;
    private PublicKey ownPublicKey;

    public ChatInstance(long userID, long ownID, DatagramSocket socket, InetAddress address, int port) throws Exception {
        super(socket, address, port);
        this.userID = userID;
        this.ownID = ownID;

        KeyPairGeneratorUtil keyGenUtil = new KeyPairGeneratorUtil();
        this.ownPrivateKey = keyGenUtil.getPrivateKey();
        this.ownPublicKey = keyGenUtil.getPublicKey();

        initiateKeyExchange();
    }

    public void initiateKeyExchange() throws IOException {
        sendKeyExchange(ownID, userID, ownPublicKey.getEncoded());
    }

    public void receivePeerPublicKey(byte[] publicKeyBytes) throws Exception {
        this.peerPublicKey = KeyUtil.convertToPublicKey(publicKeyBytes);
        System.out.println("Received public key from peer (UserID: " + userID + ")");
    }

    public void sendEncryptedMessage(String messageContent) throws Exception {
        if (peerPublicKey == null) {
            System.out.println("Error: Cannot send message, peer's public key not received.");
            return;
        }
        byte[] encryptedMessage = EncryptionUtil.encryptWithPublicKey(messageContent, peerPublicKey);
        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessage);

        sendMessage(ownID, userID, encodedMessage);
    }
}
