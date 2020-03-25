package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.services.interfaces.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class ProfileController {
    private final UsersService usersService;

    public ProfileController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/profile")
    public String showUser(Model model, HttpServletRequest req) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        HttpSession session = req.getSession(false);
        System.out.println(session.getAttribute(SPRING_SECURITY_CONTEXT_KEY));
//        Optional<UserDto> userDto = usersService.findUser(email);
//        model.addAttribute("user", userDto);
        return "profile";
    }
}
