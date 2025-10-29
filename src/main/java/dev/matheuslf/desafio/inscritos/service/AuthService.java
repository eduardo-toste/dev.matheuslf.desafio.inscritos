package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.dto.response.LoginResponse;
import dev.matheuslf.desafio.inscritos.dto.response.RegisterUserResponse;
import dev.matheuslf.desafio.inscritos.exception.ExistentUserException;
import dev.matheuslf.desafio.inscritos.mapper.AuthMapper;
import dev.matheuslf.desafio.inscritos.model.User;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       AuthMapper authMapper,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public RegisterUserResponse register(RegisterUserRequest request) {
        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new ExistentUserException();
        }

        User newUser = new User(request.name(), request.email(), passwordEncoder.encode(request.password() ));
        return authMapper.toResponse(userRepository.save(newUser));
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(auth);

        User user = (User) authentication.getPrincipal();
        return authMapper.toResponse(tokenService.generateToken(user));
    }
}
