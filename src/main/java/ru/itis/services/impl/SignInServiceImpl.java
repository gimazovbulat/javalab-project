package ru.itis.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.itis.dto.SignInForm;
import ru.itis.services.interfaces.SignInService;

@Service
public class SignInServiceImpl implements SignInService {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public SignInServiceImpl(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication signIn(SignInForm signInForm) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(signInForm.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, signInForm.getPassword(), userDetails.getAuthorities());

        Authentication res = authenticationManager.authenticate(authenticationToken);

        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(res);
            return res;
        }
        throw new IllegalStateException();
    }
}