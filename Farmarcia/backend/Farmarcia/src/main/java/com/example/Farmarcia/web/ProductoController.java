package com.example.Farmarcia.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Farmarcia.dto.ProductoRequestDto;
import com.example.Farmarcia.dto.ProductoResponseDto;
import com.example.Farmarcia.entity.StorageType;
import com.example.Farmarcia.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")

public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar Productos", description = "Lista todos los productos existentes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Listar Productos por id ", description = "Lista todos los productos existentes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un pedido y lo retorna")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDto> crearPedido(@Valid @RequestBody ProductoRequestDto pedido) {
        ProductoResponseDto nuevoPedido = productoService.crear(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @Operation(summary = "Actualizar un producto por id", description = "Actualiza un producto existente por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizarPorId(@Valid @PathVariable Long id,
            @RequestBody ProductoRequestDto request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un producto por id", description = "Elimina un producto existente por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> eliminarPorId(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar Productos por categoria", description = "Lista todos los productos existentes de una categoria específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @GetMapping("/storage/{tipo}")
    public ResponseEntity<List<ProductoResponseDto>> filtrarPorCategoria(@PathVariable StorageType tipo) {
        return ResponseEntity.ok(productoService.filtrarPorAlmacenamiento(tipo));
    }

    @Operation(summary = "Listar Productos por dias de vencimiento", description = "Lista todos los productos existentes que vencen en los próximos días")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @GetMapping("/expiring-soon/{days}")
    public ResponseEntity<List<ProductoResponseDto>> filtrarPorDiasVencimiento(@PathVariable int days) {
        return ResponseEntity.ok(productoService.productosPorVencer(days));
    }

    @Operation(summary = "Listar Productos que ya estan vencidos", description = "Lista todos los productos existentes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Pedidos no encontrados")
    })
    @GetMapping("/expired")
    public ResponseEntity<List<ProductoResponseDto>> listarProductosVencidos() {
        return ResponseEntity.ok(productoService.productosVencidos());
    }
}
