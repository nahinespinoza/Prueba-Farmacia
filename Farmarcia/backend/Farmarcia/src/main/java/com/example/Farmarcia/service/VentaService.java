package com.example.Farmarcia.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Farmarcia.dto.VentaRequestDto;
import com.example.Farmarcia.dto.VentaResponseDto;
import com.example.Farmarcia.entity.ProductoEntity;
import com.example.Farmarcia.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
public class VentaService {
    private final ProductoRepository productoRepository;

    public VentaService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public VentaResponseDto registrarVenta(VentaRequestDto request) {

        ProductoEntity producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Sistema no permite que venda si esta vencido
        if (producto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("El producto ya está vencido");
        }

        return null;
    }

}
