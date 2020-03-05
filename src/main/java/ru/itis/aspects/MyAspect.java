package ru.itis.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.FileInfo;
import ru.itis.models.User;
import ru.itis.services.impl.MailService;

import java.util.Optional;

@Component
@EnableAspectJAutoProxy
public class MyAspect {
    private final MailService mailService;
    private final UsersRepository usersRepository;

    public MyAspect(MailService mailService, UsersRepository usersRepository) {
        this.mailService = mailService;
        this.usersRepository = usersRepository;
    }

    @AfterReturning(value = "@annotation(SendMailAnno)", returning = "fileInfo")
    public void sendLinkToMail(FileInfo fileInfo){
        Optional<User> optionalUser = usersRepository.find(fileInfo.getUserId());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            mailService.sendText(user.getEmail(), fileInfo.getUrl());
        }
    }
}
