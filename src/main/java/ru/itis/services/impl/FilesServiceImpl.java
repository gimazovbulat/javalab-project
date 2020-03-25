package ru.itis.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.aspects.SendMailAnno;
import ru.itis.dao.interfaces.FilesRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.FileInfo;
import ru.itis.services.interfaces.FilesService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.apache.commons.io.IOUtils.copy;
import static org.apache.commons.io.IOUtils.resourceToByteArray;

@Service
public class FilesServiceImpl implements FilesService {
    private final FilesRepository filesRepository;

    public FilesServiceImpl(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Value("${storage.dir}")
    private String storageDir;

    @Override
    @SendMailAnno
    public FileInfo save(MultipartFile file, Long userId) {
        String fileOrigName = file.getOriginalFilename();
        String storageName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileOrigName);
        Path path = Paths.get(storageDir + storageName);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(fileOrigName)
                .storageFileName(storageName)
                .size(file.getSize())
                .type(file.getContentType())
                .url("/files/" + storageName)
                .userId(userId)
                .build();

        filesRepository.save(fileInfo);

        return fileInfo;
    }

    @Override
    public void downloadFile(HttpServletResponse response, String fileName) throws IOException {
        FileInfo fileInfo = filesRepository.findByName(fileName);
        InputStream inputStream = new FileInputStream(new File(storageDir + fileInfo.getStorageFileName()));
        copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

}
