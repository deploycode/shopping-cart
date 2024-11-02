package com.example.shopping_cart.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "discount_active", nullable = false)
    private Boolean discountActive;
}
