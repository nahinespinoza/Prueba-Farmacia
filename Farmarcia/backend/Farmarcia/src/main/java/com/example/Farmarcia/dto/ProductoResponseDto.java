package com.example.Farmarcia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.Farmarcia.entity.StorageType;

public class ProductoResponseDto {

    private Long id;
    private String nombre;
    private String ingredienteActivo;
    private StorageType tipoAlmacenamiento;
    private LocalDate fechaVencimiento;
    private BigDecimal precio;
    private Integer stock;
    private Boolean requiereReceta;

}
