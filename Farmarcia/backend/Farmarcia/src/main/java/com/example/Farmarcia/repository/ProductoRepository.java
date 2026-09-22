package com.example.Farmarcia.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Farmarcia.entity.ProductoEntity;
import com.example.Farmarcia.entity.StorageType;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    List<ProductoEntity> findByStorageType(StorageType storageType);

    List<ProductoEntity> findByExpirationDateBefore(LocalDate date);

    List<ProductoEntity> findByExpirationDateBetween(LocalDate start, LocalDate end);
}
