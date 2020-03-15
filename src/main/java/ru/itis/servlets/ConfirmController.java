package ru.itis.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.dto.UserDto;
import ru.itis.services.impl.ConfirmService;

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
    public String handleRequest(@PathVariable String link, HttpServletRequest request, ModelAndView modelAndView) {
        Optional<UserDto> optionalUser = confirmService.confirm(link);
        HttpSession session = request.getSession();
        if (optionalUser.isPresent()) {
            session.setAttribute("user", optionalUser.get());
            return "redirect:/signIn";
        } else {
            modelAndView.addObject("err", "Try again");
            return "error";
        }
    }
}
