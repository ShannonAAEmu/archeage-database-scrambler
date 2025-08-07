package info.aaemu.shannon.tools;

import info.aaemu.shannon.tools.service.BitShiftAlgorithm;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileService;
import info.aaemu.shannon.tools.service.VersionService;
import info.aaemu.shannon.tools.service.enums.CryptoMode;
import info.aaemu.shannon.tools.service.impl.BitShiftAlgorithmImpl;
import info.aaemu.shannon.tools.service.impl.CryptoServiceImpl;
import info.aaemu.shannon.tools.service.impl.FileServiceImpl;
import info.aaemu.shannon.tools.service.impl.VersionServiceImpl;
import info.aaemu.shannon.tools.service.model.ConfigVer1;
import info.aaemu.shannon.tools.service.model.ConfigVer2;

/**
 * @author Shannon
 */
public class Application {
    private static final BitShiftAlgorithm bitShiftAlgorithm = new BitShiftAlgorithmImpl();
    private static final VersionService VERSION_SERVICE = new VersionServiceImpl();
    private static final FileService fileService = new FileServiceImpl();
    private static final CryptoService cryptoService = new CryptoServiceImpl(fileService);

    public static void main(String[] args) {
        int version = VERSION_SERVICE.extractVersion(fileService.readConfigVersion());

        switch (version) {
            case 1 -> handleFirstVersion();
            case 2 -> handleSecondVersion();
            case 3 -> handleThirdVersion();
        }
    }

    private static void handleFirstVersion() {
        ConfigVer1 config = fileService.readConfig(ConfigVer1.class);

        String key = cryptoService.buildKey(config.getAesKey());

        if (CryptoMode.DECRYPT.equals(config.getCryptoMode())) {
            cryptoService.decryptType_1(key);
        } else {
            cryptoService.encryptType_1(key);
        }

        System.out.println("Provider:   " + config.getProvider());
        System.out.println("Version:    " + config.getVersion());
        System.out.println("AES Key 1:  " + config.getAesKey());
    }

    private static void handleSecondVersion() {
        ConfigVer2 config = fileService.readConfig(ConfigVer2.class);

        String aesKey_1 = generateAesKey(config.getConstants()[0], config.getAesKeySizes()[0]);
        String xorKey_1 = generateXorKey(config.getConstants()[1]);
        String aesKey_2 = generateAesKey(config.getConstants()[2], config.getAesKeySizes()[1]);
        String xorKey_2 = generateXorKey(config.getConstants()[3]);

        if (CryptoMode.DECRYPT.equals(config.getCryptoMode())) {
            cryptoService.decryptType_2(aesKey_1, xorKey_1, aesKey_2, xorKey_2);
        } else {
            cryptoService.encryptType_2(aesKey_1, xorKey_1, aesKey_2, xorKey_2);
        }

        System.out.println("Provider:   " + config.getProvider());
        System.out.println("Version:    " + config.getVersion());
        System.out.println();
        System.out.println("AES Key 1:  " + aesKey_1);
        System.out.println("XOR Key 1:  " + xorKey_1);
        System.out.println();
        System.out.println("AES Key 2:  " + aesKey_2);
        System.out.println("XOR Key 2:  " + xorKey_2);
    }

    private static void handleThirdVersion() {
        // TODO
    }

    private static String generateAesKey(String constant, int aesKeySize) {
        return bitShiftAlgorithm.shift(constant, aesKeySize);
    }

    private static String generateXorKey(String constant) {
        return bitShiftAlgorithm.shift(constant);
    }

}
