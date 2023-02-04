package info.aaemu.shannon.tools.service;

public interface FileManagerService {

    byte[] readFile(String name);

    void writeFile(byte[] data, String fileName);
}
