package com.example.Farmarcia.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/sales")
@Tag(name = "Ventas", description = "Operaciones relacionadas con ventas")

public class VentaController {

    

}
