package info.aaemu.shannon.tools.service.impl;

import info.aaemu.shannon.tools.data.property.PropertiesLoader;
import info.aaemu.shannon.tools.exception.BadParameterException;
import info.aaemu.shannon.tools.exception.NotFoundException;
import info.aaemu.shannon.tools.service.FileManagerService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManagerServiceImpl implements FileManagerService {

    @Override
    public byte[] readFile(String name) {
        validateName(name);
        String rootFolderPath = System.getProperty(PropertiesLoader.USER_DIRECTORY_NAME);
        String filePath = rootFolderPath + FileSystems.getDefault().getSeparator() + name;
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }

    private void validateName(String name) {
        if (null == name || name.isBlank()) {
            throw new BadParameterException("File name is empty");
        }
    }
}
