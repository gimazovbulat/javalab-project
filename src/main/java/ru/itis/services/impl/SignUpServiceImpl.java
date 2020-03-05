package ru.itis.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.SignUpForm;
import ru.itis.models.User;
import ru.itis.models.UserState;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SignUpServiceImpl implements SignUpService {
    private final
    UsersRepository usersRepository;
    private final
    PasswordEncoder passwordEncoder;
    private final
    JavaMailSender sender;
    private final
    Configuration ftlConfiguration;
    private static final String CONFIRM_URL = "http://localhost:8080/confirm/";

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public SignUpServiceImpl(PasswordEncoder passwordEncoder, UsersRepository usersRepository, JavaMailSender sender, Configuration ftlConfiguration) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.sender = sender;
        this.ftlConfiguration = ftlConfiguration;
    }

    @Override
    public void signUp(SignUpForm form) {
        String confirmLink = UUID.randomUUID().toString();
        String mailText =
                CONFIRM_URL + confirmLink;
        User user = User.builder()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .userState(UserState.NOT_CONFIRMED)
                .confirmLink(confirmLink)
                .build();

        usersRepository.save(user);
        StringBuilder stringBuilder = new StringBuilder();

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
                messageHelper.setFrom("lazyfucc69@gmail.com");
            } catch (javax.mail.MessagingException e) {
                throw new IllegalArgumentException(e);
            }
            sender.send(message);
        });
    }
}
