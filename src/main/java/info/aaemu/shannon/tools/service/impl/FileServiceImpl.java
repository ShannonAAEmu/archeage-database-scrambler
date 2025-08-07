package info.aaemu.shannon.tools.service.impl;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import info.aaemu.shannon.tools.service.FileService;
import lombok.SneakyThrows;

/**
 * @author Shannon
 */
public class FileServiceImpl implements FileService {
    private static final String ROOT_FOLDER = System.getProperty("user.dir");
    private final ObjectMapper objectMapper;

    public FileServiceImpl() {
        this.objectMapper = JsonMapper.builder().build();
    }

    @SneakyThrows
    @Override
    public byte[] read(String name) {
        Path filePath = fileNameToPath(name);

        return Files.readAllBytes(filePath);
    }

    @SneakyThrows
    @Override
    public void write(byte[] data, String name) {
        Path filePath = fileNameToPath(name);

        Files.deleteIfExists(filePath);
        Files.write(filePath, data, StandardOpenOption.CREATE);
    }

    @SneakyThrows
    @Override
    public String readConfigVersion() {
        byte[] bytes = read("config.json");

        JsonNode jsonNode = objectMapper.readTree(bytes);

        return jsonNode.get("version").textValue();
    }

    @SneakyThrows
    @Override
    public <T> T readConfig(Class<T> clazz) {
        byte[] bytes = read("config.json");

        return objectMapper.readValue(bytes, clazz);
    }

    private Path fileNameToPath(String name) {
        String filePath = ROOT_FOLDER + FileSystems.getDefault().getSeparator() + name;

        return Paths.get(filePath);
    }
}
