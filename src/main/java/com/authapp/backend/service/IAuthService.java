package com.authapp.backend.service;

import com.authapp.backend.dto.AuthResponse;
import com.authapp.backend.dto.LoginRequestDTO;
import com.authapp.backend.dto.RegisterRequestDTO;

public interface IAuthService {

    void register(RegisterRequestDTO request);

    AuthResponse login(LoginRequestDTO request);
}
