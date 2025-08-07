package info.aaemu.shannon.tools.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Shannon
 */
public interface FileService {

    byte[] read(String name);

    void write(byte[] data, String name);

    String readConfigVersion();

    <T> T readConfig(Class<T> clazz);

    void writeJson(ObjectNode json);
}
