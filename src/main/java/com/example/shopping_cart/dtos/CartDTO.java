package com.example.shopping_cart.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Double total;
    private List<CartItemDTO> items;
}
