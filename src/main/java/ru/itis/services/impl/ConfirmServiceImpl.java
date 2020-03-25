package ru.itis.services.impl;

import org.springframework.stereotype.Component;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.models.UserState;
import ru.itis.services.interfaces.ConfirmService;

import java.util.Optional;

@Component
public class ConfirmServiceImpl implements ConfirmService {
    private final UsersRepository usersRepository;

    public ConfirmServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean confirm(String confirmLink) {
        Optional<User> optionalUser = usersRepository.findByConfirmLink(confirmLink);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserState(UserState.CONFIRMED);
            usersRepository.update(user);
           return true;
        }
        throw new IllegalStateException();
    }
}
