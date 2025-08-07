package info.aaemu.shannon.tools.service.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import info.aaemu.shannon.tools.service.AesEncryption;
import lombok.SneakyThrows;

/**
 * @author Shannon
 */
public class AesEncryptionImpl implements AesEncryption {
    private SecretKeySpec secretKey;
    private Cipher cipher;

    @SneakyThrows
    @Override
    public void setKey(byte[] key, String transformation) {
        secretKey = new SecretKeySpec(key, "AES");
        cipher = Cipher.getInstance(transformation);
    }

    @SneakyThrows
    @Override
    public byte[] decryptType_1(byte[] encryptedData) {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        return cipher.doFinal(encryptedData);
    }

    @SneakyThrows
    @Override
    public byte[] encryptType_1(byte[] decryptedData) {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        return cipher.doFinal(decryptedData);
    }

    @SneakyThrows
    @Override
    public byte[] decryptType_2(byte[] encryptedData) {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(encryptedData);
    }

    @SneakyThrows
    @Override
    public byte[] encryptType_2(byte[] decryptedData) {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(decryptedData);
    }
}
