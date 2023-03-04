package info.aaemu.shannon.tools.service;

import info.aaemu.shannon.tools.controller.entity.EncryptionKey;

public interface CryptoService {

    byte APP_VERSION_ONE = 1;
    byte APP_VERSION_TWO = 2;

    void decrypt(String fileName, String key);

    void encrypt(String fileName, String key);

    void decryptNew(String fileName, EncryptionKey encryptionKey);

    void encryptNew(String fileName, EncryptionKey encryptionKey);
}
