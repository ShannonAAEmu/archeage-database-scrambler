package info.aaemu.shannon.tools;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.factory.Factory;

public class Main {

    public static void main(String[] args) {
        UiController uiController = Factory.INSTANCE.getUiController();
        uiController.showGameInfo();
        uiController.showCryptoMode();
        byte cryptoMode = uiController.inputCryptoMode();
        uiController.cryptoAction(cryptoMode);
    }
}
