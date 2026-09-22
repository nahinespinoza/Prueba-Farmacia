package com.example.Farmarcia.dto;

import java.time.LocalDateTime;

public class VentaResponseDto {

    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Long vendedorId;
    private String nombreVendedor;
    private Integer cantidad;
    private LocalDateTime fechaVenta;

}
