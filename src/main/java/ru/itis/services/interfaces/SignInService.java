package ru.itis.services.interfaces;

import org.springframework.security.core.Authentication;
import ru.itis.dto.SignInForm;

public interface SignInService {
    Authentication signIn(SignInForm signInForm);
}
