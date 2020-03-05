package ru.itis.services.impl;

import ru.itis.dto.UserDto;

import java.util.Optional;

public interface ConfirmService {
    Optional<UserDto> isConfirmed(String confirmLink);
}
