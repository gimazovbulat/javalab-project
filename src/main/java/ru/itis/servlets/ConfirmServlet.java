package ru.itis.servlets;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.dto.UserDto;
import ru.itis.services.impl.ConfirmService;
import ru.itis.services.impl.ConfirmServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@WebServlet(name = "confirmServlet", urlPatterns = "/confirm/*")
public class ConfirmServlet extends HttpServlet {
    private ConfirmService confirmService;
    private freemarker.template.Configuration cfg;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        ApplicationContext applicationContext =
                (ApplicationContext) servletContext.getAttribute("applicationContext");
        confirmService = applicationContext.getBean(ConfirmServiceImpl.class);
        cfg = applicationContext.getBean(freemarker.template.Configuration.class);
        cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<UserDto> optionalUser = confirmService.isConfirmed(req.getPathInfo().substring(1));
        HttpSession session = req.getSession();
        if (optionalUser.isPresent()){
            session.setAttribute("user", optionalUser.get());
            resp.sendRedirect("/signIn");
        }
        else {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "something went wrong");
            String content = FreeMarkerTemplateUtils
                    .processTemplateIntoString(cfg.getTemplate("error.ftl"), model);
            resp.getWriter().write(content);
        }
    }
}
