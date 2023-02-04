package info.aaemu.shannon.tools.controller.factory;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.impl.UiControllerImpl;
import info.aaemu.shannon.tools.service.CryptoService;
import info.aaemu.shannon.tools.service.FileManagerService;
import info.aaemu.shannon.tools.service.GameInfoService;
import info.aaemu.shannon.tools.service.impl.CryptoServiceImpl;
import info.aaemu.shannon.tools.service.impl.FileManagerServiceImpl;
import info.aaemu.shannon.tools.service.impl.GameInfoServiceImpl;

public enum Factory {
    INSTANCE;

    private final UiController uiController;

    Factory() {
        GameInfoService gameInfoService = new GameInfoServiceImpl();
        FileManagerService fileManagerService = new FileManagerServiceImpl();
        CryptoService cryptoService = new CryptoServiceImpl(fileManagerService);
        this.uiController = new UiControllerImpl(gameInfoService, cryptoService);
    }

    public UiController getUiController() {
        return uiController;
    }
}
