package com.example.Farmarcia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VentaResponseDto {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Long vendedorId;
    private String nombreVendedor;
    private Integer cantidad;
    private LocalDateTime fechaVenta;

}
