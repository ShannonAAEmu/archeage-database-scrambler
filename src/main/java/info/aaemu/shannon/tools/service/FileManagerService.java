package info.aaemu.shannon.tools.service;

public interface FileManagerService {

    boolean isAvailableFile(String name);

    byte[] readFile(String name);

    void writeFile(byte[] data, String name);
}
