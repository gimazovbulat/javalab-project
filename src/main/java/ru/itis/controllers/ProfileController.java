package ru.itis.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.dto.UserDto;
import ru.itis.services.interfaces.UsersService;

import java.util.Optional;

@Controller
public class ProfileController {
    private final UsersService usersService;

    public ProfileController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/profile")
    public String showUser(Model model, Authentication authentication) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<UserDto> optionalUser = usersService.findUser(email);
            optionalUser.ifPresent(userDto -> model.addAttribute("user", userDto));
            return "profile";
        }
}
