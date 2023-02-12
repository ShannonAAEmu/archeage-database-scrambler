package info.aaemu.shannon.tools;

import info.aaemu.shannon.tools.controller.factory.Factory;

public class Main {

    public static void main(String[] args) {
        Factory.INSTANCE.getUiController().initConsole();
    }
}
