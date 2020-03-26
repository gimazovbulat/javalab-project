package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.dto.UserDto;
import ru.itis.services.interfaces.ConfirmService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ConfirmController {
    private final ConfirmService confirmService;

    public ConfirmController(ConfirmService confirmService) {
        this.confirmService = confirmService;
    }

    @GetMapping("/confirm/{link}")
    public String handleRequest(@PathVariable String link) {
        boolean isConfirmed = confirmService.confirm(link);
        if (isConfirmed) {
            return "redirect:/signIn";
        } else {
           return "error";
        }
    }
}
