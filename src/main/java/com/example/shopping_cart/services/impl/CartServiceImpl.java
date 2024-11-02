package com.example.shopping_cart.services.impl;

import com.example.shopping_cart.dtos.CartDTO;
import com.example.shopping_cart.dtos.CartItemDTO;
import com.example.shopping_cart.entities.Cart;
import com.example.shopping_cart.entities.CartItem;
import com.example.shopping_cart.entities.Product;
import com.example.shopping_cart.entities.User;
import com.example.shopping_cart.exceptions.BusinessRuleException;
import com.example.shopping_cart.repositories.CartItemRepository;
import com.example.shopping_cart.repositories.CartRepository;
import com.example.shopping_cart.repositories.ProductRepository;
import com.example.shopping_cart.repositories.UserRepository;
import com.example.shopping_cart.services.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public CartDTO createCart(Long userId) throws BusinessRuleException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            throw new BusinessRuleException(HttpStatus.PRECONDITION_FAILED, "El usuario ya tiene un carrito de compras asociado.");
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotal(0.0);

        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }

    @Override
    public CartDTO getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return convertToDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            // Si el producto no está en el carrito, se agrega uno nuevo
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setSubtotal(product.getUnitPrice() * quantity);
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
        } else {
            // Si el producto ya está en el carrito, se actualiza la cantidad
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setSubtotal(cartItem.getQuantity() * product.getUnitPrice());
            cartItemRepository.save(cartItem);
        }

        // Actualizar el total del carrito
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        cart.setTotal(total);

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantity(Long cartId, Long productId, int quantity)  throws BusinessRuleException {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (quantity < 0) {
            throw new BusinessRuleException(HttpStatus.PRECONDITION_FAILED, "La cantidad de productos ha agregar debe ser mayor a 0");
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException(HttpStatus.PRECONDITION_FAILED, "El producto que desea actualizar no existe en el carrito"));

        if (quantity <= 0) {
            // Si la cantidad es 0 o menos, se elimina el producto del carrito
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            // Actualizar la cantidad y el subtotal
            cartItem.setQuantity(quantity);
            cartItem.setSubtotal(quantity * cartItem.getProduct().getUnitPrice());
            cartItemRepository.save(cartItem);
        }

        // Actualizar el total del carrito
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        cart.setTotal(total);

        return convertToDTO(cartRepository.save(cart));
    }


    @Override
    @Transactional
    public CartDTO removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        // Eliminar el producto del carrito y del repositorio de items
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        // Actualizar el total del carrito
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        cart.setTotal(total);

        return convertToDTO(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDTO clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
        cart.setTotal(0.0);
        cart.getItems().clear();
        return convertToDTO(cartRepository.save(cart));
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setTotal(cart.getTotal());

        List<CartItemDTO> items = cart.getItems().stream().map(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPrice(item.getProduct().getUnitPrice());
            itemDTO.setSubtotal(item.getSubtotal());
            return itemDTO;
        }).collect(Collectors.toList());

        cartDTO.setItems(items);
        return cartDTO;
    }
}
