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
        String encryptionKeyFromProperty = key.replaceAll("\\s+", "");
        validateEncryptionKeyFromProperty(encryptionKeyFromProperty);
        byte[] encryptionKey = getKey(encryptionKeyFromProperty);
        byte[] encryptedData = fileManagerService.readFile(fileName);
        byte[] decryptedData = decrypt(encryptedData, encryptionKey);
        writeDecryptedData(decryptedData);
    }

    private byte[] getKey(String key) {
        return DatatypeConverter.parseHexBinary(key);
    }

    private byte[] decrypt(byte[] encryptedData, byte[] encryptionKey) {
        aesService.setKey(encryptionKey);
        return aesService.decrypt(encryptedData);
    }

    private void writeDecryptedData(byte[] decryptedData) {
        fileManagerService.writeFile(decryptedData, PropertiesLoader.INSTANCE.getKey(PropertiesLoader.DATABASE_TARGET_NAME));
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
