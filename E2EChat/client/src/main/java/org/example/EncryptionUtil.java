package org.example;

import javax.crypto.Cipher;
import java.security.PublicKey;

public class EncryptionUtil {

    public static byte[] encryptWithPublicKey(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }
}
