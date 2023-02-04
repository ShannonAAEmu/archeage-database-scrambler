package info.aaemu.shannon.tools.service;

public interface CryptoService {

    void decrypt(String fileName, String key);

    void encrypt(String fileName, String key);
}
