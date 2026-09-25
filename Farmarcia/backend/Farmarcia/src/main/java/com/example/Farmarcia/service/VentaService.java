package com.example.Farmarcia.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.Farmarcia.dto.VentaRequestDto;
import com.example.Farmarcia.dto.VentaResponseDto;
import com.example.Farmarcia.entity.ProductoEntity;
import com.example.Farmarcia.entity.Role;
import com.example.Farmarcia.entity.UsuarioEntity;
import com.example.Farmarcia.entity.VentaEntity;
import com.example.Farmarcia.exception.BusinessException;
import com.example.Farmarcia.exception.ResourceNotFoundException;
import com.example.Farmarcia.repository.ProductoRepository;
import com.example.Farmarcia.repository.UsuarioRepository;
import com.example.Farmarcia.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
public class VentaService {
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    public VentaService(ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            VentaRepository ventaRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    public VentaResponseDto registrarVenta(VentaRequestDto venta) {

        // Valida si existe el producto
        ProductoEntity producto = productoRepository.findById(venta.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Sistema no permite que venda si esta vencido
        if (producto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException("El producto ya está vencido");

        }

        // Sistema no permite que venda si no hay stock
        if (producto.getStock() < venta.getCantidad()) {
            throw new BusinessException("No hay suficiente stock disponible");
        }

        producto.setStock(producto.getStock() - venta.getCantidad());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new IllegalStateException("No hay un usuario autenticado");
        }

        UsuarioEntity vendedor = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        VentaEntity ventaEntity = VentaEntity.builder()
                .product(producto)
                .soldBy(vendedor)
                .quantity(venta.getCantidad())
                .saleDate(LocalDateTime.now())
                .build();

        VentaEntity ventaGuardada = ventaRepository.save(ventaEntity);
        return mapToResponse(ventaGuardada);
    }

    public VentaResponseDto obtenerVentaPorId(Long id) {
        VentaEntity venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
        return mapToResponse(venta);
    }

    public List<VentaResponseDto> listarVentas() {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<VentaEntity> ventas;
        if (usuario.getRole() == Role.ADMIN) {
            ventas = ventaRepository.findAll();
        } else {
            ventas = ventaRepository.findBySoldById(usuario.getId());
        }
        return ventas.stream().map(this::mapToResponse).toList();
    }

    private VentaResponseDto mapToResponse(VentaEntity venta) {
        VentaResponseDto response = new VentaResponseDto();
        response.setId(venta.getId());
        response.setProductoId(venta.getProduct().getId());
        response.setNombreProducto(venta.getProduct().getName());
        response.setVendedorId(venta.getSoldBy().getId());
        response.setNombreVendedor(venta.getSoldBy().getName());
        response.setCantidad(venta.getQuantity());
        response.setFechaVenta(venta.getSaleDate());
        return response;
    }
}
