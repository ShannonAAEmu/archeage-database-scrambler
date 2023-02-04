package info.aaemu.shannon.tools.service;

public interface AesService {
    String ALGORITHM = "AES";
    String TRANSFORMATION_CBC = "AES/CBC/NoPadding";

    void setKey(byte[] key);

    byte[] decrypt(byte[] encryptedData);
}
