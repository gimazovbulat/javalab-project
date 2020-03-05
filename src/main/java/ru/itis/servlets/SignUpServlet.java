package ru.itis.servlets;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.dto.SignUpForm;
import ru.itis.services.impl.SignUpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("/signUp")
public class SignUpServlet implements Controller {
    private final SignUpService signUpService;

    public SignUpServlet(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        if (request.getMethod().equals("GET")){
            return modelAndView;
        }
        else {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            signUpService.signUp(
                    SignUpForm.builder()
                            .email(email)
                            .password(password)
                            .build()
            );
            return null;
        }
    }
}
