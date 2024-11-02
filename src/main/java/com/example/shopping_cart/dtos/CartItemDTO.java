package com.example.shopping_cart.dtos;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
