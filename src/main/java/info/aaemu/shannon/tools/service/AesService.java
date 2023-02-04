package info.aaemu.shannon.tools.service;

public interface AesService {
    String ALGORITHM = "AES";
    String CBC = "CBC";
    String NO_PADDING = "NoPadding";
    String TRANSFORMATION_CBC_NO_PADDING = ALGORITHM + "/" + CBC + "/" + NO_PADDING;

    void setKey(byte[] key);

    byte[] decrypt(byte[] encryptedData);

    byte[] encrypt(byte[] decryptedData);
}
