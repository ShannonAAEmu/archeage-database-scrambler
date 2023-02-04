package info.aaemu.shannon.tools.controller.impl;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.entity.GameInfo;
import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.GameInfoService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UiControllerImpl implements UiController {
    private final GameInfoService gameInfoService;
    private final CryptoService cryptoService;

    @Override
    public GameInfo getGameInfo() {
        return gameInfoService.getGameInfo();
    }

    @Override
    public void decryptDatabase() {
        cryptoService.decrypt(PropertiesLoader.DATABASE_SOURCE_NAME, PropertiesLoader.ENCRYPTION_KEY);
    }
}
