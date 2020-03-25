package ru.itis.services.interfaces;

import ru.itis.dto.UserDto;

import java.util.Optional;

public interface ConfirmService {
    boolean confirm(String confirmLink);
}
