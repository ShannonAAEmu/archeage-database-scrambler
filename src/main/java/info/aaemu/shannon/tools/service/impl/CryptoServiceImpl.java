package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.exception.BadPropertyValue;
import info.aaemu.shannon.tools.service.AesService;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;

@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {
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
        String encryptionKeyFromProperty = key.replaceAll("\\s+", "");
        validateEncryptionKeyFromProperty(encryptionKeyFromProperty);
        return DatatypeConverter.parseHexBinary(encryptionKeyFromProperty);
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

    private void validateParams(String fileName, String key) {
        if (null == fileName || null == key || fileName.isBlank() || key.isBlank()) {
            throw new BadParameterException("File name or key is empty");
        }
    }

    private void validateEncryptionKeyFromProperty(String encryptionKeyFromProperty) {
        if (16 > encryptionKeyFromProperty.length()) {
            throw new BadPropertyValue("Invalid encryption key length");
        }
    }
}
