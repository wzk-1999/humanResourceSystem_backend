package com.blueOcean.humanResourceSystem.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static SecretKey secretKey;

    static {
        // Load the encryption key from environment variable
        String encodedKey = System.getenv("ENCRYPTION_KEY");
        if (encodedKey != null) {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        } else {
            throw new RuntimeException("Environment variable ENCRYPTION_KEY is not set");
        }
    }

    // Encrypt a double value and return it as a Base64 encoded string
    public static String encrypt(double value) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(Double.toString(value).getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt a Base64 encoded string and return it as a double value
    public static double decrypt(String encryptedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return Double.parseDouble(new String(decryptedBytes));
    }
}

