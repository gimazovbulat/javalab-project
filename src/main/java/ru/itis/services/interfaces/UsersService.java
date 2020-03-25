package ru.itis.services.interfaces;

import ru.itis.dto.UserDto;

import java.util.Optional;

public interface UsersService {
    Optional<UserDto> findUser(Long id);
    Optional<UserDto> findUser(String email);
}
