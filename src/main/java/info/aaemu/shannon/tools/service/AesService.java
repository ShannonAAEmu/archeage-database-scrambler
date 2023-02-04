package info.aaemu.shannon.tools.service;

public interface AesService {
    String ALGORITHM = "AES";
    String TRANSFORMATION = "AES/ECB/NoPadding";

    void setKey(byte[] key);

    byte[] decrypt(byte[] encryptedData, byte[] key);
}
