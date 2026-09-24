package com.example.Farmarcia.service;

import org.springframework.stereotype.Service;

import com.example.Farmarcia.dto.RegisterRequestDto;
import com.example.Farmarcia.dto.UsarioResponseDto;
import com.example.Farmarcia.entity.Role;
import com.example.Farmarcia.entity.UsuarioEntity;
import com.example.Farmarcia.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service 
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacionService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }  

    public UsuarioEntity crearUsuario(RegisterRequestDto dto) {
        // Verificar si el correo ya está registrado
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setName(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Asignar Rol de Cliente por defecto
        if(dto.getRol() != Role.ADMIN || dto.getRol() == null) {    
            usuario.setRole(Role.USER);
        } else {
            usuario.setRole(Role.ADMIN);
        }
        return usuarioRepository.save(usuario);
    }

    //devolver info del usuario logeado

    public UsarioResponseDto obtenerUsuarioLogeado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new IllegalStateException("No hay un usuario autenticado");
        }

        UsuarioEntity usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsarioResponseDto response = new UsarioResponseDto();
        response.setId(usuario.getId());
        response.setNombre(usuario.getName());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRole().name());
        return response;

    }

}
