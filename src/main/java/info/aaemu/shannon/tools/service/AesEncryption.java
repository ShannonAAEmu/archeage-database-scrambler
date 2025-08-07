package info.aaemu.shannon.tools.service;

/**
 * @author Shannon
 */
public interface AesEncryption {

    void setKey(byte[] key, String transformation);

    byte[] decryptType_1(byte[] encryptedData);

    byte[] encryptType_1(byte[] decryptedData);

    byte[] decryptType_2(byte[] encryptedData);

    byte[] encryptType_2(byte[] decryptedData);
}
