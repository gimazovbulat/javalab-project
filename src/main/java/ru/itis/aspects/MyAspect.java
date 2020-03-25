package ru.itis.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.models.FileInfo;
import ru.itis.models.User;
import ru.itis.services.impl.MailService;

import java.util.Optional;

@Component
@Aspect
public class MyAspect {
    private final MailService mailService;
    private final UsersRepository usersRepository;

    @Value("${web.root}")
    private String webRoot;

    public MyAspect(MailService mailService, UsersRepository usersRepository) {
        this.mailService = mailService;
        this.usersRepository = usersRepository;
    }

    @AfterReturning(value = "@annotation(SendMailAnno)", returning = "fileInfo")
    public void sendLinkToMail(FileInfo fileInfo){
        Optional<User> optionalUser = usersRepository.find(fileInfo.getUserId());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            mailService.sendText(user.getEmail(), webRoot + fileInfo.getUrl());
        }
    }
}
