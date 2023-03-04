package info.aaemu.shannon.tools.controller.impl;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.entity.EncryptionKey;
import info.aaemu.shannon.tools.controller.entity.GameInfo;
import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.ApplicationException;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import info.aaemu.shannon.tools.service.GameInfoService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UiControllerImpl implements UiController {
    private static final byte DECRYPT_MODE = 1;
    private static final byte ENCRYPT_MODE = 2;
    private final GameInfoService gameInfoService;
    private final FileManagerService fileManagerService;
    private final CryptoService cryptoService;
    private boolean canDecrypt;
    private boolean canEncrypt;
    private Scanner scanner;

    @Override
    public void showGameInfo() {
        GameInfo gameInfo = getGameInfo();
        print("Game: " + gameInfo.getName());
        print("Version: " + gameInfo.getVersion());
        print("Provider: " + gameInfo.getProvider());
        print("");
    }

    @Override
    public void showCryptoMode() {
        canDecrypt = isCanDecrypt();
        if (canDecrypt) {
            print("Select 1: Decrypt");
        }
        canEncrypt = isCanEncrypt();
        if (canEncrypt) {
            print("Select 2: Encrypt");
        }
        if (!canDecrypt && !canEncrypt) {
            exit("Database(s) not found!");
        }
    }

    @Override
    public byte inputCryptoMode() {
        try {
            return getScanner().nextByte();
        } catch (Exception e) {
            throw new BadParameterException(e);
        }
    }

    @Override
    public void cryptoAction(byte cryptoMode) {
        try {
            if (DECRYPT_MODE == cryptoMode && canDecrypt) {
                print("Start decrypting...");
                decryptDatabase();
            } else if (ENCRYPT_MODE == cryptoMode && canEncrypt) {
                print("Start encrypting...");
                encryptDatabase();
            } else {
                exit("Invalid crypto mode!");
            }
        } catch (ApplicationException e) {
            exit("Invalid application.properties file!");
        }
    }

    @Override
    public void decryptDatabase() {
        String fileName = getFileName(PropertiesLoader.DATABASE_SOURCE_NAME);
        byte appVersion = getAppVersion();
        if (appVersion == CryptoService.APP_VERSION_ONE) {
            String key = getKey();
            cryptoService.decrypt(fileName, key);
        }
        if (appVersion == CryptoService.APP_VERSION_TWO) {
            EncryptionKey encryptionKey = getEncryptionKey();
            cryptoService.decryptNew(fileName, encryptionKey);
        } else {
            throw new ApplicationException("Unknown app version!");
        }
    }

    @Override
    public void encryptDatabase() {
        String fileName = getFileName(PropertiesLoader.DATABASE_TARGET_NAME);
        byte appVersion = getAppVersion();
        if (appVersion == CryptoService.APP_VERSION_ONE) {
            String key = getKey();
            cryptoService.encrypt(fileName, key);
        }
        if (appVersion == CryptoService.APP_VERSION_TWO) {
            EncryptionKey encryptionKey = getEncryptionKey();
            cryptoService.encryptNew(fileName, encryptionKey);
        } else {
            throw new ApplicationException("Unknown app version!");
        }
    }

    private void print(String s) {
        System.out.println(s);
    }

    private GameInfo getGameInfo() {
        return gameInfoService.getGameInfo();
    }

    private boolean isCanDecrypt() {
        return fileManagerService.isAvailableFile(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_SOURCE_NAME));
    }

    private boolean isCanEncrypt() {
        return fileManagerService.isAvailableFile(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME));
    }

    private Scanner getScanner() {
        if (null == scanner) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    private String getKey() {
        String key;
        byte appVersion = Byte.parseByte(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.APP_VERSION));
        if (CryptoService.APP_VERSION_ONE == appVersion) {
            key = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY);
        } else if (CryptoService.APP_VERSION_TWO == appVersion) {
            key = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_NEW_ROUND_ONE_AES);
        } else {
            throw new ApplicationException("Property file don't contain encryption key!");
        }
        return key;
    }

    private String getFileName(String key) {
        return PropertiesLoader.INSTANCE.getKey(key);
    }

    private byte getAppVersion() {
        return Byte.parseByte(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.APP_VERSION));
    }

    private EncryptionKey getEncryptionKey() {
        EncryptionKey encryptionKey = new EncryptionKey(new String[2], new String[2]);
        encryptionKey.getAesKeyArray()[0] = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_NEW_ROUND_ONE_AES);
        encryptionKey.getAesKeyArray()[1] = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_NEW_ROUND_TWO_AES);
        encryptionKey.getXorKeyArray()[0] = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_NEW_ROUND_ONE_XOR);
        encryptionKey.getXorKeyArray()[1] = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_NEW_ROUND_TWO_XOR);
        return encryptionKey;
    }

    private void exit(String msg) {
        print("\n" + msg);
        print("\nEnter any key to exit...");
        getScanner().next();
        System.exit(0);
    }
}
