package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.service.AesService;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;

@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {
    private static final byte APP_VERSION_ONE = 1;
    private final FileManagerService fileManagerService;
    private final AesService aesService;

    @Override
    public void decrypt(String fileName, String key) {
        validateParams(fileName, key);
        byte[] encryptionKey = getKey(key);
        byte[] encryptedData = fileManagerService.readFile(fileName);
        byte[] decryptedData = decryptData(encryptedData, encryptionKey);
        writeData(decryptedData, PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME));
    }

    @Override
    public void encrypt(String fileName, String key) {
        validateParams(fileName, key);
        byte[] encryptionKey = getKey(key);
        byte[] decryptedData = fileManagerService.readFile(fileName);
        byte[] encryptedData = encryptData(decryptedData, encryptionKey);
        writeData(encryptedData, PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_NEW_NAME));
    }

    private byte[] getKey(String key) {
        String validKey;
        if (isRawKey()) {
            validKey = buildKey(key);
        } else {
            validKey = key.replaceAll("\\s+", "");
        }
        return hexStringToByteArray(validKey);
    }

    private String buildKey(String key) {
        String validKey = null;
        if (APP_VERSION_ONE == getAppVersion()) {
            String[] strings = key.split("\\s+");
            String[] temp = new String[16];
            temp[0] = strings[7];
            temp[1] = strings[14];
            temp[2] = strings[1];
            temp[3] = strings[12];
            temp[4] = strings[3];
            temp[5] = strings[6];
            temp[6] = strings[11];
            temp[7] = strings[10];
            temp[8] = strings[4];
            temp[9] = strings[9];
            temp[10] = strings[13];
            temp[11] = strings[5];
            temp[12] = strings[2];
            temp[13] = strings[15];
            temp[14] = strings[8];
            temp[15] = strings[0];
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : temp) {
                stringBuilder.append(s);
            }
            validKey = stringBuilder.toString();
        }
        return validKey;
    }

    private byte[] decryptData(byte[] encryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey);
        return aesService.decrypt(encryptedData);
    }

    private byte[] encryptData(byte[] decryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey);
        return aesService.encrypt(decryptedData);
    }

    private void writeData(byte[] decryptedData, String fileName) {
        fileManagerService.writeFile(decryptedData, fileName);
    }

    private boolean isRawKey() {
        return Boolean.parseBoolean(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY_RAW));
    }

    private byte getAppVersion() {
        return Byte.parseByte(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.APP_VERSION));
    }

    private byte[] hexStringToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    private void validateParams(String fileName, String key) {
        if (null == fileName || null == key || fileName.isBlank() || key.isBlank()) {
            throw new BadParameterException("File name or key is empty");
        }
    }
}
