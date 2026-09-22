package com.example.Farmarcia.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Farmarcia.dto.ProductoResponseDto;
import com.example.Farmarcia.service.ProductoService;

@RestController
@RequestMapping("/products")
public class ProductoController {

    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponseDto> listarProductos() {
        return productoService.listarTodos();
    }

}
