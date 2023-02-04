package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.exception.BadPropertyValue;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;

@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {
    private final FileManagerService fileManagerService;

    @Override
    public void decrypt(String fileName, String key) {
        validateParams(fileName, key);
        String encryptionKeyString = PropertiesLoader.INSTANCE.getKey(PropertiesLoader.ENCRYPTION_KEY).replaceAll("\\s+", "");
        validateEncryptionKey(encryptionKeyString);
        byte[] encryptedData = fileManagerService.readFile(fileName);
        byte[] encryptionKey = DatatypeConverter.parseHexBinary(encryptionKeyString);
    }

    private void validateParams(String fileName, String key) {
        if (null == fileName || null == key || fileName.isBlank() || key.isBlank()) {
            throw new BadParameterException("File name or key is empty");
        }
    }

    private void validateEncryptionKey(String encryptionKeyString) {
        if (16 > encryptionKeyString.length()) {
            throw new BadPropertyValue("Invalid encryption key length");
        }
    }
}
