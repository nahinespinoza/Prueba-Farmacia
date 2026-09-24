package com.example.Farmarcia.dto;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor

public class UsarioResponseDto {

    private Long id;
    private String nombre;
    private String email;
    private String rol;

}
