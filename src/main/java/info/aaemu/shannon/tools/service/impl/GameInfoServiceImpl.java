package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.controller.entity.GameInfo;
import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.service.GameInfoService;

public class GameInfoServiceImpl implements GameInfoService {

    @Override
    public GameInfo getGameInfo() {
        GameInfo gameInfo = new GameInfo();
        gameInfo.setName(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.GAME_NAME));
        gameInfo.setVersion(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.GAME_VERSION));
        gameInfo.setProvider(PropertiesLoader.INSTANCE.getKey(PropertiesLoader.GAME_PROVIDER));
        return gameInfo;
    }
}
