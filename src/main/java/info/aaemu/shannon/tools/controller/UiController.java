package info.aaemu.shannon.tools.controller;

import info.aaemu.shannon.tools.controller.entity.GameInfo;

public interface UiController {

    void initConsole();

    GameInfo getGameInfo();

    void decryptDatabase();

    void encryptDatabase();
}
