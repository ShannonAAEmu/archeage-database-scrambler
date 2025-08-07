package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.service.VersionService;

/**
 * @author Shannon
 */
public class VersionServiceImpl implements VersionService {

    @Override
    public int extractVersion(String version) {
        if (version.startsWith("0.") || version.startsWith("1.")) {
            return 1;
        }

        if (version.startsWith("2.")) {
            return 2;
        }

        return 3;
    }
}
