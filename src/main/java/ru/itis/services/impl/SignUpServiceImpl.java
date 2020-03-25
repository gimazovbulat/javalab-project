package ru.itis.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.SignUpForm;
import ru.itis.models.FileInfo;
import ru.itis.models.User;
import ru.itis.models.UserState;
import ru.itis.security.Role;
import ru.itis.services.interfaces.FilesService;
import ru.itis.services.interfaces.SignUpService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class SignUpServiceImpl implements SignUpService {
    private final
    UsersRepository usersRepository;
    private final
    PasswordEncoder passwordEncoder;
    private final
    MailService mailService;
    private final
    FilesService filesService;

    public SignUpServiceImpl(PasswordEncoder passwordEncoder, UsersRepository usersRepository, MailService mailService, FilesService filesService) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.mailService = mailService;
        this.filesService = filesService;
    }

    @Override
    public void signUp(SignUpForm form) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        String confirmLink = UUID.randomUUID().toString();
        User user = User.builder()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .userState(UserState.NOT_CONFIRMED)
                .confirmLink(confirmLink)
                .avaPath("default")
                .roles(roles)
                .build();

        Long userId = usersRepository.save(user);

        FileInfo fileInfo = filesService.save(form.getFile(), userId);
        user.setAvaPath(fileInfo.getUrl());
        usersRepository.update(user);

        mailService.sendEmailConfirmationLink(user);
    }
}
