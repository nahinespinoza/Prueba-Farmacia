package com.example.Farmarcia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "El email es obligatorio")
    @Email
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

}
