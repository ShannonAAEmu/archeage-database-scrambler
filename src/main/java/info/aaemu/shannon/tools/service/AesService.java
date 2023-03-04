package info.aaemu.shannon.tools.service;

public interface AesService {
    String ALGORITHM = "AES";
    String CBC = "CBC";
    String ECB = "ECB";
    String NO_PADDING = "NoPadding";
    String TRANSFORMATION_CBC_NO_PADDING = ALGORITHM + "/" + CBC + "/" + NO_PADDING;
    String TRANSFORMATION_ECB_NO_PADDING = ALGORITHM + "/" + ECB + "/" + NO_PADDING;

    void setKey(byte[] key, String transformation);

    byte[] decrypt(byte[] encryptedData);

    byte[] encrypt(byte[] decryptedData);

    byte[] decryptNew(byte[] encryptedData);

    byte[] encryptNew(byte[] decryptedData);
}
