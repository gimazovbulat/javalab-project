package ru.itis.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.aspects.SendMailAnno;
import ru.itis.dao.interfaces.FilesRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.FileInfo;
import ru.itis.services.interfaces.FilesService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FilesServiceImpl implements FilesService {
    private final FilesRepository filesRepository;

    public FilesServiceImpl(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Override
    public File find(String origFileName) {
        FileInfo fileInfo = filesRepository.findByName(origFileName);
        return new File(fileInfo.getStorageFileName());
    }

    @SendMailAnno
    @Override
    public FileInfo save(MultipartFile file, UserDto userDto) {
        String storageName = "C:/savedFiles/" + UUID.randomUUID() + file.getOriginalFilename();
        Path path = Paths.get("C:/savedFiles/" + storageName);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(file.getOriginalFilename())
                .storageFileName(storageName)
                .size(file.getSize())
                .type(file.getContentType())
                .url("localhost:8080/files/" + storageName)
                .userId(userDto.getId())
                .build();
        filesRepository.save(fileInfo);
        return fileInfo;
    }
}
