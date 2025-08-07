package info.aaemu.shannon.tools.service.util;

import java.util.HexFormat;

import lombok.experimental.UtilityClass;

/**
 * @author Shannon
 */
@UtilityClass
public class HexUtils {

    public static String toHex(int value) {
        return Integer.toHexString(value).toUpperCase();
    }

    public static byte[] toByteArray(String hex) {
        return HexFormat.of().parseHex(hex);
    }
}
