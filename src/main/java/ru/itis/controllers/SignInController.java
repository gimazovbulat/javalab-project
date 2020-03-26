package ru.itis.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    @GetMapping("/signIn")
    public String getPage(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/profile";
        }
        return "signIn";
    }
}
