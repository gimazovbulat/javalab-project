package ru.itis.services.impl;

import org.springframework.stereotype.Service;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.services.interfaces.UsersService;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Optional<UserDto> findUser(Long id) {
        Optional<User> optionalUser = usersRepository.find(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return Optional.of(UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .avaPath(user.getAvaPath())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findUser(String email) {
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return Optional.of(UserDto.builder()
                    .email(email)
                    .avaPath(user.getAvaPath())
                    .id(user.getId())
                    .build());
        }
        return Optional.empty();

    }
}