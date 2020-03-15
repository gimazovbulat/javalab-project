package ru.itis.servlets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.dto.SignUpForm;
import ru.itis.services.impl.SignUpService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping("/signUp")
    public String getPage() {
        return "home";
    }

    @PostMapping("/signUp")
    public String handleRequest(SignUpForm signUpForm, Model model) {
        signUpService.signUp(signUpForm);
        model.addAttribute("status", "link sent");
        return "home";
    }
}
