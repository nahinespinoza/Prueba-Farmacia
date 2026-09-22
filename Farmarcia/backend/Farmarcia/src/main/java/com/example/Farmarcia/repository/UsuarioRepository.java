package com.example.Farmarcia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Farmarcia.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
