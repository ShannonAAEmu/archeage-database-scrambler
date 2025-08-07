package info.aaemu.shannon.tools.service.impl;

import java.util.Arrays;

import info.aaemu.shannon.tools.service.AesEncryption;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileService;
import info.aaemu.shannon.tools.service.util.HexUtils;

/**
 * @author Shannon
 */
public class CryptoServiceImpl implements CryptoService {
    private final AesEncryption aesEncryption;
    private final FileService fileService;

    public CryptoServiceImpl(FileService fileService) {
        this.aesEncryption = new AesEncryptionImpl();
        this.fileService = fileService;
    }

    @Override
    public String buildKey(String key) {
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

        StringBuilder sb = new StringBuilder();
        for (String s : temp) {
            sb.append(s);
        }

        return sb.toString();
    }

    @Override
    public void decryptType_1(String key) {
        aesEncryption.setKey(HexUtils.toByteArray(key), "AES/CBC/NoPadding");

        byte[] encryptedData = fileService.read("compact.sqlite3");

        writeData(aesEncryption.decryptType_1(encryptedData), "compact_decrypted.sqlite3");
    }

    @Override
    public void encryptType_1(String key) {
        aesEncryption.setKey(HexUtils.toByteArray(key), "AES/CBC/NoPadding");

        byte[] decryptedData = fileService.read("compact_decrypted.sqlite3");

        writeData(aesEncryption.encryptType_1(decryptedData), "compact_new.sqlite3");
    }

    @Override
    public void decryptType_2(String rawAesKey_1, String rawXorKey_1, String rawAesKey_2, String rawXorKey_2) {
        aesEncryption.setKey(HexUtils.toByteArray(rawAesKey_1), "AES/ECB/NoPadding");
        byte[] xorKey = HexUtils.toByteArray(rawXorKey_1);

        byte[] encryptedData = fileService.read("compact.sqlite");
        int offset = getOffset(HexUtils.toHex(encryptedData.length));

        byte[] data = decryptRound(encryptedData, offset, xorKey);

        aesEncryption.setKey(HexUtils.toByteArray(rawAesKey_2), "AES/ECB/NoPadding");
        xorKey = HexUtils.toByteArray(rawXorKey_2);

        offset = 0;

        writeData(decryptRound(data, offset, xorKey), "compact.zip");
    }

    @Override
    public void encryptType_2(String rawAesKey_1, String rawXorKey_1, String rawAesKey_2, String rawXorKey_2) {
        aesEncryption.setKey(HexUtils.toByteArray(rawAesKey_2), "AES/ECB/NoPadding");
        byte[] xorKey = HexUtils.toByteArray(rawXorKey_2);

        byte[] decryptedData = fileService.read("compact.zip");
        String decryptedHexSize = HexUtils.toHex(decryptedData.length);
        int offset = 0;

        byte[] data = encryptRound(decryptedData, offset, xorKey);

        aesEncryption.setKey(HexUtils.toByteArray(rawAesKey_1), "AES/ECB/NoPadding");
        xorKey = HexUtils.toByteArray(rawXorKey_1);

        offset = getOffset(decryptedHexSize);

        writeData(encryptRound(data, offset, xorKey), "compact_new.sqlite");
    }

    private byte[] decryptRound(byte[] encryptedData, int offset, byte[] xorKey) {
        byte[] data;
        byte[] decryptedData;

        while (offset + 16 <= encryptedData.length) {
            data = Arrays.copyOfRange(encryptedData, offset, offset + 16);

            xor(xorKey, data);

            decryptedData = aesEncryption.decryptType_2(data);

            System.arraycopy(decryptedData, 0, encryptedData, offset, 16);

            xorKey = decryptedData;

            offset += 16;
        }

        return encryptedData;
    }

    private byte[] encryptRound(byte[] decryptedData, int offset, byte[] xorKey) {
        while (offset + 16 <= decryptedData.length) {
            xorKey = encrypt(decryptedData, offset, xorKey);
            offset += 16;
        }
        return decryptedData;
    }

    private byte[] encrypt(byte[] decryptedData, int offset, byte[] xorKey) {
        byte[] data = Arrays.copyOfRange(decryptedData, offset, offset + 16);

        byte[] nextXorKey = data;

        data = aesEncryption.encryptType_2(data);

        xor(xorKey, data);

        System.arraycopy(data, 0, decryptedData, offset, data.length);

        return nextXorKey;
    }

    private void xor(byte[] key, byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) (data[i] ^ key[i]);
        }
    }

    private byte getOffset(String hexSize) {
        String hexStr = hexSize.substring(hexSize.length() - 1);
        if (1 == hexStr.length()) {
            hexStr = "0" + hexStr;
        }
        return (byte) (16 - HexUtils.toByteArray(hexStr)[0]);
    }

    private void writeData(byte[] decryptedData, String fileName) {
        fileService.write(decryptedData, fileName);
    }
}
