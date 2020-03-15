package ru.itis.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.dto.UserDto;
import ru.itis.services.interfaces.FilesService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

@Controller
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping(value = "/files")
    public ModelAndView uploadFile() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("files");
        return modelAndView;
    }

    @PostMapping(value = "/files")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        filesService.save(multipartFile, userDto);
        ModelAndView model = new ModelAndView();
        model.addObject("status", "file was successfully downloaded");
        model.setViewName("files");
        return model;
    }
    // localhost:8080/files/123809183093qsdas09df8af.jpeg

    @GetMapping(value = "/files/{file-name:.+}")
    public void getFile(@PathVariable("file-name") String fileName, HttpServletResponse response) {
        try {
            filesService.downloadFile(response, fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}