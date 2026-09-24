package com.example.Farmarcia.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductoEntity product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sold_by", nullable = false)
    private UsuarioEntity soldBy;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate = LocalDateTime.now();

    
}