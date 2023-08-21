package com.guardias.backend.controlador;

import org.springframework.stereotype.Service;

import com.guardias.backend.jwt.JwtService;
import com.guardias.backend.repositorio.UsuarioRepositorio;
import com.guardias.backend.user.Role;
import com.guardias.backend.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        return null;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .country(request.getCountry())
        .role(Role.USER)
        .build();

        usuarioRepositorio.save(user);
        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }

}
