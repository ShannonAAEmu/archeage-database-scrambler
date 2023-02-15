package info.aaemu.shannon.tools.controller.impl;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.entity.GameInfo;
import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import info.aaemu.shannon.tools.service.GameInfoService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UiControllerImpl implements UiController {
    private final static byte DECRYPT_MODE = 1;
    private final static byte ENCRYPT_MODE = 2;
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
        if (DECRYPT_MODE == cryptoMode && canDecrypt) {
            print("Start decrypting...");
            decryptDatabase();
        } else if (ENCRYPT_MODE == cryptoMode && canEncrypt) {
            print("Start encrypting...");
            encryptDatabase();
        } else {
            exit("Invalid crypto mode!");
        }
    }

    @Override
    public void decryptDatabase() {
        cryptoService.decrypt(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_SOURCE_NAME), PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY));
    }

    @Override
    public void encryptDatabase() {
        cryptoService.encrypt(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME), PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY));
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

    private void exit(String msg) {
        print("\n" + msg);
        print("\nEnter any key to exit...");
        getScanner().next();
        System.exit(0);
    }

    private void print(String s) {
        System.out.println(s);
    }
}
