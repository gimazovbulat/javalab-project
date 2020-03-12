package ru.itis.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.UserDto;
import ru.itis.models.FileInfo;

import java.io.File;

public interface FilesService {
   File find(String origFileName);
   FileInfo save(MultipartFile file, UserDto userDto);
}
