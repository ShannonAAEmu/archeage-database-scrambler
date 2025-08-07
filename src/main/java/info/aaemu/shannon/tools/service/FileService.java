package info.aaemu.shannon.tools.service;

/**
 * @author Shannon
 */
public interface FileService {

    byte[] read(String name);

    void write(byte[] data, String name);

    String readConfigVersion();

    <T> T readConfig(Class<T> clazz);
}
