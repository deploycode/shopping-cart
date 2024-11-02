package com.example.shopping_cart.services;

import com.example.shopping_cart.dtos.CartDTO;

public interface CartService {
    CartDTO createCart(Long userId);
    CartDTO getCart(Long cartId);
    CartDTO addProductToCart(Long cartId, Long productId, int quantity);
    CartDTO updateProductQuantity(Long cartId, Long productId, int quantity);
    CartDTO removeProductFromCart(Long cartId, Long productId);
    CartDTO clearCart(Long cartId);
}
