package com.example.Farmarcia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Farmarcia.entity.VentaEntity;

public interface VentaRepository extends JpaRepository<VentaEntity, Long> {

    List<VentaEntity> findBySoldById(Long userId);
}
