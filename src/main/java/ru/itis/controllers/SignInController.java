package ru.itis.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.dto.SignInForm;
import ru.itis.services.interfaces.SignInService;

@Controller
public class SignInController {
    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @GetMapping("/signIn")
    public String getPage(Authentication authentication) {
        if (authentication != null){
            return "redirect:/signIn";
        }
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signIn(SignInForm signInForm) {
        Authentication auth = signInService.signIn(signInForm);
        if (auth != null) {
            return "redirect:/profile";
        }
        return "signIn";
    }
}
