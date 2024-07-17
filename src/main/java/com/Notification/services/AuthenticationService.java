package com.Notification.services;

import com.Notification.domain.User;
import com.Notification.dtos.user.AuthenticationDTO;
import com.Notification.dtos.user.LoginResponseDTO;
import com.Notification.dtos.user.RegisterDTO;
import com.Notification.repositories.UserRepository;
import com.Notification.security.TokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository repository, @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public LoginResponseDTO loginUser(AuthenticationDTO data){
        if (repository.findByLogin(data.login()) == null) return null;

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generate((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }


    public User registerUser(RegisterDTO data)
    {
        if (repository.findByLogin(data.login()) != null) return null;

        String passwordBcrypt = new BCryptPasswordEncoder().encode(data.password());

        User userRegister = new User(data.login(), passwordBcrypt, data.role());

        return this.repository.save(userRegister);
    }
}
