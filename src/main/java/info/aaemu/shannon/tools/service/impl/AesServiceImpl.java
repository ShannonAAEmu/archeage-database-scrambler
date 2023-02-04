package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.exception.ApplicationException;
import info.aaemu.shannon.tools.service.AesService;
import lombok.RequiredArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
public class AesServiceImpl implements AesService {

    private SecretKeySpec secretKey;
    private Cipher cipher;

    @Override
    public void setKey(byte[] key) {
        secretKey = new SecretKeySpec(key, AesService.ALGORITHM);
        try {
            cipher = Cipher.getInstance(AesService.TRANSFORMATION);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, byte[] key) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedData);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ApplicationException(e);
        }
    }
}
