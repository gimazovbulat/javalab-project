package ru.itis.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.models.User;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {
    private static final String CONFIRM_URL = "http://localhost:8080/confirm/";
    private final
    JavaMailSender sender;
    private final
    Configuration ftlConfiguration;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public MailService(JavaMailSender sender, Configuration ftlConfiguration) {
        this.sender = sender;
        this.ftlConfiguration = ftlConfiguration;
    }

    @Value("${mail.username}")
    private String FROM;

    public void sendEmailConfirmationLink(User user){
        StringBuilder stringBuilder = new StringBuilder();

        String mailText =
                CONFIRM_URL + user.getConfirmLink();

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("link", mailText);
        try {

            stringBuilder.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(ftlConfiguration.getTemplate("mail.ftl"), model));
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
        executorService.submit(() -> {
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setTo(user.getEmail());
                messageHelper.setSubject("Подтерждение регистрации");
                messageHelper.setText(stringBuilder.toString(), true);
                messageHelper.setFrom(FROM);
            } catch (javax.mail.MessagingException e) {
                throw new IllegalArgumentException(e);
            }
            sender.send(message);
        });
    }
    public void sendText(String email, String text){
        executorService.submit(() -> {
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setTo(email);
                messageHelper.setSubject("Подтерждение регистрации");
                messageHelper.setText(text, true);
                messageHelper.setFrom(FROM);
            } catch (javax.mail.MessagingException e) {
                throw new IllegalArgumentException(e);
            }
            sender.send(message);
        });
    }
}
