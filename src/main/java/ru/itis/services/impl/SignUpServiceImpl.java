package ru.itis.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.SignUpForm;
import ru.itis.models.User;
import ru.itis.models.UserState;
import ru.itis.security.Role;
import ru.itis.services.interfaces.MailService;
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

    public SignUpServiceImpl(PasswordEncoder passwordEncoder, UsersRepository usersRepository, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.mailService = mailService;
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

        usersRepository.save(user);

        mailService.sendEmailConfirmationLink(user);
    }
}
