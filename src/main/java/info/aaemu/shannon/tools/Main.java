package info.aaemu.shannon.tools;

import info.aaemu.shannon.tools.controller.UiController;
import info.aaemu.shannon.tools.controller.entity.GameInfo;
import info.aaemu.shannon.tools.controller.factory.Factory;

public class Main {

    private static UiController uiController;

    public static void main(String[] args) {
        uiController = Factory.INSTANCE.getUiController();
        GameInfo gameInfo = getGameInfo();
        showGameInfo(gameInfo);
        decryptDatabase();
        encryptDatabase();
    }

    private static GameInfo getGameInfo() {
        return uiController.getGameInfo();
    }

    private static void showGameInfo(GameInfo gameInfo) {
        System.out.println(gameInfo);
    }

    private static void decryptDatabase() {
        uiController.decryptDatabase();
    }

    private static void encryptDatabase() {
        uiController.encryptDatabase();
    }
}
