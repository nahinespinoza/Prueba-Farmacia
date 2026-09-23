package com.example.Farmarcia.dto;

import com.example.Farmarcia.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    String jwt;
    Long idUsuario;
    Role rol;
}
