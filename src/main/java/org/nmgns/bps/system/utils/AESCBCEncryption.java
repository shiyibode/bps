package org.nmgns.bps.system.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCBCEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = DefaultConfig.APP_KEY; // 16 bytes key
    private static final String IV = DefaultConfig.APP_KEY; // 16 bytes IV sail

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original);
    }

}
