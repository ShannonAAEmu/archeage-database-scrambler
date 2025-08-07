package info.aaemu.shannon.tools.service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import info.aaemu.shannon.tools.service.enums.CryptoMode;
import lombok.Data;

/**
 * @author Shannon
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfigVer2 {
    private String provider;
    private String version;
    private String[] constants;
    private Integer[] aesKeySizes;
    private CryptoMode cryptoMode;
}
