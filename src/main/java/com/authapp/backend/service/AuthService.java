package com.authapp.backend.service;

import com.authapp.backend.dto.AuthResponse;
import com.authapp.backend.dto.LoginRequestDTO;
import com.authapp.backend.dto.RegisterRequestDTO;
import com.authapp.backend.model.AuthProvider;
import com.authapp.backend.model.Role;
import com.authapp.backend.model.UserEntity;
import com.authapp.backend.repository.UserRepository;
import com.authapp.backend.security.JwtUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilsService jwtUtilsService;

    @Override
    public void register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already in use");
        }

        UserEntity user = UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .provider(AuthProvider.LOCAL)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtilsService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

}
