package com.example.Farmarcia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Farmarcia.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
        Optional<UsuarioEntity> findByEmail(String email);

}
