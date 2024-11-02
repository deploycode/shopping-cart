package com.example.shopping_cart.services;

import com.example.shopping_cart.dtos.CartDTO;
import com.example.shopping_cart.exceptions.BusinessRuleException;

public interface CartService {
    CartDTO createCart(Long userId) throws BusinessRuleException;
    CartDTO getCart(Long cartId);
    CartDTO addProductToCart(Long cartId, Long productId, int quantity);
    CartDTO updateProductQuantity(Long cartId, Long productId, int quantity) throws BusinessRuleException;
    CartDTO removeProductFromCart(Long cartId, Long productId);
    CartDTO clearCart(Long cartId);
}
