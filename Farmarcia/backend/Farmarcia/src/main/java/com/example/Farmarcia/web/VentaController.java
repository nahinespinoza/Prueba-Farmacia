package com.example.Farmarcia.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Farmarcia.dto.VentaRequestDto;
import com.example.Farmarcia.dto.VentaResponseDto;
import com.example.Farmarcia.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
@Tag(name = "Ventas", description = "Operaciones relacionadas con ventas")
@SecurityRequirement(name = "bearerAuth")

public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Crear un nueva venta", description = "Registra una nueva venta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Venta no válida")
    })
    @PostMapping
    public ResponseEntity<VentaResponseDto> crearVenta(@Valid @RequestBody VentaRequestDto venta) {
        VentaResponseDto nuevaVenta = ventaService.registrarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @Operation(summary = "Obtener venta por ID", description = "Obtiene una venta por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDto> obtenerVentaPorId(@PathVariable Long id) {
        VentaResponseDto venta = ventaService.obtenerVentaPorId(id);
        return ResponseEntity.ok(venta);
    }

    @Operation(summary = "Listar todas las ventas", description = "Lista todas las ventas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas encontradas"),
            @ApiResponse(responseCode = "404", description = "Ventas no encontradas")
    })
    @GetMapping
    public ResponseEntity<List<VentaResponseDto>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

}
