package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.controller.entity.EncryptionKey;
import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.service.AesService;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {
    private static final byte APP_VERSION_ONE = 1;
    private static final byte MAX_XOR_SIZE = 16;
    private final FileManagerService fileManagerService;
    private final AesService aesService;
    private byte[] xorCommonKey;

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

    @Override
    public void decryptNew(String fileName, EncryptionKey encryptionKey) {
        validateParams(fileName, encryptionKey);
        byte[] encryptedData = fileManagerService.readFile(fileName);
        int encryptedDataSize = encryptedData.length;
        String encryptedDataHexSize = Integer.toHexString(encryptedDataSize).toUpperCase();
        int offset = getOffset(encryptedDataHexSize);
        byte[] xorKey = hexStringToByteArray(encryptionKey.getXorKeyArray()[0]);
        byte[] aesKey = getKey(encryptionKey.getAesKeyArray()[0]);
        byte[] roundedEncryptedData = decryptRound(encryptedData, offset, xorKey, aesKey);
        offset = 0;
        xorKey = hexStringToByteArray(encryptionKey.getXorKeyArray()[1]);
        aesKey = getKey(encryptionKey.getAesKeyArray()[1]);
        byte[] decryptedData = decryptRound(roundedEncryptedData, offset, xorKey, aesKey);
        writeData(decryptedData, PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME));
    }

    @Override
    public void encryptNew(String fileName, EncryptionKey encryptionKey) {
        validateParams(fileName, encryptionKey);
        byte[] decryptedData = fileManagerService.readFile(fileName);
        int decryptedDataSize = decryptedData.length;
        String decryptedDataHexSize = Integer.toHexString(decryptedDataSize).toUpperCase();
        int offset = 0;
        byte[] xorKey = hexStringToByteArray(encryptionKey.getXorKeyArray()[1]);
        byte[] aesKey = getKey(encryptionKey.getAesKeyArray()[1]);
        byte[] roundedDecryptedData = encryptRound(decryptedData, offset, xorKey, aesKey);
        offset = getOffset(decryptedDataHexSize);
        xorKey = hexStringToByteArray(encryptionKey.getXorKeyArray()[0]);
        aesKey = getKey(encryptionKey.getAesKeyArray()[0]);
        byte[] encryptedData = encryptRound(roundedDecryptedData, offset, xorKey, aesKey);
        writeData(encryptedData, PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_NEW_NAME));
    }

    private byte[] decryptRound(byte[] encryptedData, int offset, byte[] xorKey, byte[] aesKey) {
        byte[] data;
        byte[] decryptedData;
        while (offset + MAX_XOR_SIZE <= encryptedData.length) {
            data = Arrays.copyOfRange(encryptedData, offset, offset + MAX_XOR_SIZE);
            xor(xorKey, data);
            decryptedData = decryptDataNew(data, aesKey);
            System.arraycopy(decryptedData, 0, encryptedData, offset, MAX_XOR_SIZE);
            xorKey = decryptedData;
            offset += MAX_XOR_SIZE;
        }
        return encryptedData;
    }

    private byte[] encryptRound(byte[] decryptedData, int offset, byte[] xorKey, byte[] aesKey) {
        xorCommonKey = xorKey;
        while (offset + MAX_XOR_SIZE <= decryptedData.length) {
            encrypt(decryptedData, offset, xorCommonKey, aesKey);
            offset += MAX_XOR_SIZE;
        }
        return decryptedData;
    }

    private void encrypt(byte[] decryptedData, int offset, byte[] xorKey, byte[] aesKey) {
        byte[] data = Arrays.copyOfRange(decryptedData, offset, offset + MAX_XOR_SIZE);
        xorCommonKey = data;
        data = encryptDataNew(data, aesKey);
        xor(xorKey, data);
        System.arraycopy(data, 0, decryptedData, offset, data.length);
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
        aesService.setKey(encryptionKey, AesService.TRANSFORMATION_CBC_NO_PADDING);
        return aesService.decrypt(encryptedData);
    }

    private byte[] encryptData(byte[] decryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey, AesService.TRANSFORMATION_CBC_NO_PADDING);
        return aesService.encrypt(decryptedData);
    }

    private byte[] decryptDataNew(byte[] encryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey, AesService.TRANSFORMATION_ECB_NO_PADDING);
        return aesService.decryptNew(encryptedData);
    }

    private byte[] encryptDataNew(byte[] decryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey, AesService.TRANSFORMATION_ECB_NO_PADDING);
        return aesService.encryptNew(decryptedData);
    }

    private byte getOffset(String hexSize) {
        String hexStr = hexSize.substring(hexSize.length() - 1);
        if (1 == hexStr.length()) {
            hexStr = "0" + hexStr;
        }
        return (byte) (16 - hexStringToByteArray(hexStr)[0]);
    }

    private void xor(byte[] key, byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) (data[i] ^ key[i]);
        }
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

    private void validateParams(String fileName, EncryptionKey encryptionKey) {
        if (null == fileName || null == encryptionKey || fileName.isBlank()
                || null == encryptionKey.getAesKeyArray() || null == encryptionKey.getXorKeyArray()) {
            throw new BadParameterException("File name or key(s) is empty");
        }
    }
}
