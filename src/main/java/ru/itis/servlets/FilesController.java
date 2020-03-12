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

import javax.servlet.http.HttpSession;
import java.io.File;

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
        return null;
    }
    // localhost:8080/files/123809183093qsdas09df8af.jpeg

    @GetMapping(value = "/files/{file-name:.+}")
    public ModelAndView getFile(@PathVariable("file-name") String fileName) {
        File file = filesService.find(fileName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("files");
        modelAndView.addObject("file", file);

        return modelAndView;
    }
}