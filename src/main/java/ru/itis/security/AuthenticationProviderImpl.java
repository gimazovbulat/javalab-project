package ru.itis.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dao.interfaces.UsersRepository;
import ru.itis.models.User;
import ru.itis.models.UserState;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationProviderImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (!optionalUser.isPresent()){
            throw new BadCredentialsException("1000");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("1000");
        }
        if (user.getUserState() == UserState.NOT_CONFIRMED){
            throw new DisabledException("1001");
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getVal()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
