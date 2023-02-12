package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.ApplicationException;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.exception.NotFoundException;
import info.aaemu.shannon.tools.service.FileManagerService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileManagerServiceImpl implements FileManagerService {
    private final String rootFolder = System.getProperty(PropertiesLoader.USER_DIRECTORY_NAME);

    @Override
    public boolean isAvailableFile(String name) {
        validateName(name);
        String filePath = rootFolder + FileSystems.getDefault().getSeparator() + name;
        return Files.exists(Paths.get(filePath));
    }

    @Override
    public byte[] readFile(String name) {
        validateName(name);
        if (!isAvailableFile(name)) {
            throw new NotFoundException("File " + name + " not found");
        }
        String filePath = rootFolder + FileSystems.getDefault().getSeparator() + name;
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeFile(byte[] data, String name) {
        validateData(data);
        String filePathString = rootFolder + FileSystems.getDefault().getSeparator() + name;
        Path filePath = Paths.get(filePathString);
        try {
            Files.deleteIfExists(filePath);
            Files.write(filePath, data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private void validateName(String name) {
        if (null == name || name.isBlank()) {
            throw new BadParameterException("File name is empty");
        }
    }

    private void validateData(byte[] data) {
        if (null == data) {
            throw new BadParameterException("Write data is empty");
        }
    }
}
