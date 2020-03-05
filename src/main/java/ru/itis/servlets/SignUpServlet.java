package ru.itis.servlets;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.dto.SignUpForm;
import ru.itis.services.impl.SignUpService;
import ru.itis.services.impl.SignUpServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "signUpServlet", urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {
    private SignUpService signUpService;
    private freemarker.template.Configuration cfg;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        ApplicationContext applicationContext =
                (ApplicationContext) servletContext.getAttribute("applicationContext");
        signUpService = applicationContext.getBean(SignUpServiceImpl.class);
        cfg = applicationContext.getBean(freemarker.template.Configuration.class);
        cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
    }

    @SneakyThrows
    @Override
    @Bean
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = FreeMarkerTemplateUtils
                .processTemplateIntoString(cfg.getTemplate("home"), null);
        resp.getWriter().write(content);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        signUpService.signUp(
                SignUpForm.builder()
                .email(email)
                .password(password)
                .build()
        );
    }
}
