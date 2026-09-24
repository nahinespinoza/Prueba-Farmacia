package com.example.Farmarcia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.Farmarcia.entity.StorageType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El ingrediente activo es obligatorio")
    private String ingredienteActivo;

    @NotNull(message = "El tipo de almacenamiento es obligatorio")
    private StorageType tipoAlmacenamiento;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La fecha de vencimiento debe ser futura")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "Debe indicar si requiere receta")
    private Boolean requiereReceta;

}
