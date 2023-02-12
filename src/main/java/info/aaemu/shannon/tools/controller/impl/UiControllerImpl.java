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
    private final GameInfoService gameInfoService;
    private final FileManagerService fileManagerService;
    private final CryptoService cryptoService;
    private boolean canDecrypt;
    private boolean canEncrypt;

    @Override
    public void initConsole() {
        showGameInfo();
        showActions();
        selectAction(getMode());
    }

    @Override
    public GameInfo getGameInfo() {
        return gameInfoService.getGameInfo();
    }

    @Override
    public void decryptDatabase() {
        cryptoService.decrypt(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_SOURCE_NAME), PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY));
    }

    @Override
    public void encryptDatabase() {
        cryptoService.encrypt(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME), PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY));
    }

    private void showGameInfo() {
        System.out.println(getGameInfo());
    }

    private void showActions() {
        canDecrypt = fileManagerService.isAvailableFile(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_SOURCE_NAME));
        canEncrypt = fileManagerService.isAvailableFile(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME));
        if (canDecrypt) {
            System.out.println("Select 1: Decrypt");
        }
        if (canEncrypt) {
            System.out.println("Select 2: Encrypt");
        }
        if (!canDecrypt && !canEncrypt) {
            System.out.println("Databases not found");
            System.exit(0);
        }
    }

    private byte getMode() {
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextByte();
        } catch (Exception e) {
            throw new BadParameterException(e);
        }
    }

    private void selectAction(byte mode) {
        if (canDecrypt && 1 == mode) {
            System.out.println("Start decrypting...");
            decryptDatabase();
        }
        if (canEncrypt && 2 == mode) {
            System.out.println("Start encrypting...");
            encryptDatabase();
        }
    }
}
