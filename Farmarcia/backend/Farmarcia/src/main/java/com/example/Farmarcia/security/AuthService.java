package com.example.Farmarcia.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.Farmarcia.dto.LoginRequestDto;
import com.example.Farmarcia.dto.LoginResponseDto;
import com.example.Farmarcia.entity.UsuarioEntity;
import com.example.Farmarcia.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UsuarioRepository usuarioRepository;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        // Autenticación del usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()));

        UsuarioEntity user = usuarioRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = authUtil.generateAccesToken(user);

        return new LoginResponseDto(token, user.getId(),user.getRole());

    }

}
