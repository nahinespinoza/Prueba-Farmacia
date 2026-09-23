package com.example.Farmarcia.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Farmarcia.dto.ProductoRequestDto;
import com.example.Farmarcia.dto.ProductoResponseDto;
import com.example.Farmarcia.entity.ProductoEntity;
import com.example.Farmarcia.entity.StorageType;
import com.example.Farmarcia.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;

    }

    public List<ProductoResponseDto> listarTodos() {
        List<ProductoEntity> productos = productoRepository.findAll();
        List<ProductoResponseDto> respuesta = new ArrayList<>();

        for (ProductoEntity producto : productos) {
            respuesta.add(mapToResponse(producto));
        }
        return respuesta;
    }

    public ProductoResponseDto obtenerPorId(Long id) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToResponse(producto);
    }

    public ProductoResponseDto crear(ProductoRequestDto request) {
        ProductoEntity producto = new ProductoEntity();
        producto.setName(request.getNombre());
        producto.setActiveIngredient(request.getIngredienteActivo());
        producto.setStorageType(request.getTipoAlmacenamiento());
        producto.setExpirationDate(request.getFechaVencimiento());
        producto.setPrice(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setRequiresPrescription(request.getRequiereReceta());

        ProductoEntity guardado = productoRepository.save(producto);
        return mapToResponse(guardado);
    }

    // Actualizar por id

    public ProductoResponseDto actualizar(Long id, ProductoRequestDto request) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setName(request.getNombre());
        producto.setActiveIngredient(request.getIngredienteActivo());
        producto.setStorageType(request.getTipoAlmacenamiento());
        producto.setExpirationDate(request.getFechaVencimiento());
        producto.setPrice(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setRequiresPrescription(request.getRequiereReceta());

        ProductoEntity actualizado = productoRepository.save(producto);
        return mapToResponse(actualizado);
    }

    // Borrar por id

    public void eliminar(Long id) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(producto);
    }

    // Obtener por categoria

    public List<ProductoResponseDto> filtrarPorAlmacenamiento(StorageType tipo) {
        List<ProductoEntity> productos = productoRepository.findByStorageType(tipo);
        List<ProductoResponseDto> respuesta = new ArrayList<>();

        for (ProductoEntity producto : productos) {
            respuesta.add(mapToResponse(producto));
        }
        return respuesta;
    }

    // productos por vencer

    public List<ProductoResponseDto> productosPorVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);

        List<ProductoEntity> productos = productoRepository.findByExpirationDateBetween(hoy, limite);
        List<ProductoResponseDto> respuesta = new ArrayList<>();

        for (ProductoEntity producto : productos) {
            respuesta.add(mapToResponse(producto));
        }
        return respuesta;
    }

    public List<ProductoResponseDto> productosVencidos() {
        List<ProductoEntity> productos = productoRepository.findByExpirationDateBefore(LocalDate.now());
        List<ProductoResponseDto> respuesta = new ArrayList<>();

        for (ProductoEntity producto : productos) {
            respuesta.add(mapToResponse(producto));
        }
        return respuesta;
    }
    // Mapeo por hacer

    private ProductoResponseDto mapToResponse(ProductoEntity producto) {
        ProductoResponseDto response = new ProductoResponseDto();
        response.setId(producto.getId());
        response.setNombre(producto.getName());
        response.setIngredienteActivo(producto.getActiveIngredient());
        response.setTipoAlmacenamiento(producto.getStorageType());
        response.setFechaVencimiento(producto.getExpirationDate());
        response.setPrecio(producto.getPrice());
        response.setStock(producto.getStock());
        response.setRequiereReceta(producto.getRequiresPrescription());

        return response;

    }

}
