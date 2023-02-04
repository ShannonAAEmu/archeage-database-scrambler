package info.aaemu.shannon.tools.data.property;

import info.aaemu.shannon.tools.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public enum PropertiesLoader {
    INSTANCE;

    public static final String APP_VERSION = "app.version";
    public static final String USER_DIRECTORY_NAME = "user.dir";
    public static final String GAME_NAME = "game.name";
    public static final String GAME_VERSION = "game.version";
    public static final String GAME_PROVIDER = "game.provider";
    public static final String DATABASE_SOURCE_NAME = "database.source.name";
    public static final String DATABASE_TARGET_NAME = "database.target.name";
    public static final String DATABASE_NEW_NAME = "database.new.name";
    public static final String ENCRYPTION_KEY = "encryption.key";
    public static final String ENCRYPTION_KEY_RAW = "encryption.key.raw";
    private static final String PROPERTIES_NAME = "application.properties";
    private final Properties properties;

    PropertiesLoader() {
        this.properties = new Properties();
        String rootFolderPath = System.getProperty(USER_DIRECTORY_NAME);
        String propertyFilePath = rootFolderPath + FileSystems.getDefault().getSeparator() + PROPERTIES_NAME;
        try (InputStream inputStream = Files.newInputStream(Paths.get(propertyFilePath))) {
            this.properties.load(inputStream);
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }

    public String getKey(String key) {
        return properties.getProperty(key);
    }
}
