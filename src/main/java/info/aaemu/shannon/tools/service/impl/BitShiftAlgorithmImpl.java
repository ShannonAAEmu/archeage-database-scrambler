package info.aaemu.shannon.tools.service.impl;

import java.util.ArrayList;
import java.util.List;

import info.aaemu.shannon.tools.service.BitShiftAlgorithm;

/**
 * @author Shannon
 */
public class BitShiftAlgorithmImpl implements BitShiftAlgorithm {
    private final long xlRandomConstant;

    public BitShiftAlgorithmImpl() {
        this.xlRandomConstant = Long.parseLong("49616E42", 16);
    }

    @Override
    public String shift(String constant, int aesKeySize) {
        long value = Long.parseLong(constant, 16);
        int roundCount = aesKeySize / 32;

        return shift(value, roundCount);
    }

    @Override
    public String shift(String constant) {
        long value = Long.parseLong(constant, 16);
        int roundCount = 4;

        return shift(value, roundCount);
    }

    private String shift(long value, int roundCount) {
        long xlRandom = xlRandom(value);
        String xlRandomGenerated = xlRandomGenerate(value, xlRandom, roundCount);

        return reverseHexString(xlRandomGenerated);
    }

    private long xlRandom(long firstLong) {
        return firstLong ^ xlRandomConstant;
    }

    private String xlRandomGenerate(long firstConstant, long xlRandom, int roundCount) {
        List<String> list = new ArrayList<>(roundCount);
        StringBuilder sb = new StringBuilder(8 * roundCount);

        for (int i = 0; i < roundCount; i++) {
            long shiftRight = firstConstant >>> 16;
            long shiftLeft = firstConstant << 16;

            long firstAdd = shiftRight + xlRandom;
            long secondAdd = (shiftLeft + firstAdd) & 0xFFFFFFFFL;
            long thirdAdd = (secondAdd + xlRandom) & 0xFFFFFFFFL;

            firstConstant = secondAdd;
            xlRandom = thirdAdd;

            list.add(String.format("%08X", firstConstant));
        }

        list = list.reversed();

        for (String s : list) {
            sb.append(s);
        }
        list.clear();

        return sb.toString();
    }

    private String reverseHexString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must not be null and must have an even length.");
        }

        StringBuilder reversedHex = new StringBuilder(hexString.length());
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexByte = hexString.substring(i, i + 2);
            reversedHex.insert(0, hexByte);
        }

        return reversedHex.toString();
    }
}
