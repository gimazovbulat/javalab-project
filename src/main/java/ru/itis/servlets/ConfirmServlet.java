package ru.itis.servlets;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.dto.UserDto;
import ru.itis.services.impl.ConfirmService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Component("/confirm/*")
public class ConfirmServlet implements Controller {
    private final ConfirmService confirmService;

    public ConfirmServlet(ConfirmService confirmService) {
        this.confirmService = confirmService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (req.getMethod().equals("GET")) {
            Optional<UserDto> optionalUser = confirmService.isConfirmed(req.getPathInfo().substring(1));
            HttpSession session = req.getSession();
            if (optionalUser.isPresent()) {
                session.setAttribute("user", optionalUser.get());
                resp.sendRedirect("/signIn");
            } else {

            }
        }
        throw new MethodNotAllowedException(HttpMethod.POST, Collections.singleton(HttpMethod.GET));
    }
}
