package info.aaemu.shannon.tools.controller;

public interface UiController {

    void showGameInfo();

    void showCryptoMode();

    byte inputCryptoMode();

    void cryptoAction(byte cryptoMode);

    void decryptDatabase();

    void encryptDatabase();
}
