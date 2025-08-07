package info.aaemu.shannon.tools.service;

/**
 * @author Shannon
 */
public interface CryptoService {

    String buildKey(String key);

    void decryptType_1(String key);

    void encryptType_1(String key);

    void decryptType_2(String rawAesKey_1, String rawXorKey_1, String rawAesKey_2, String rawXorKey_2);

    void encryptType_2(String rawAesKey_1, String rawXorKey_1, String rawAesKey_2, String rawXorKey_2);
}
