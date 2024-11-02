package com.example.shopping_cart.controllers;

import com.example.shopping_cart.dtos.CartDTO;
import com.example.shopping_cart.exceptions.BusinessRuleException;
import com.example.shopping_cart.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carritos de compras", description = "Operaciones relacionadas con la gestión de carritos de compras")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "0) Crear un nuevo carrito.", description = "Crea un nuevo carrito de compras vacío asociado a un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/create")
    public ResponseEntity<CartDTO> createCart(
            @Parameter(description = "ID del usuario") @RequestParam Long userId) throws BusinessRuleException {
        CartDTO cartDTO = cartService.createCart(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @Operation(summary = "4) Ver Carrito de Compras.", description = "Retorna los detalles del carrito de compras, incluyendo productos, cnatidades, precios, y total.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El carrito ha sido encontrado."),
            @ApiResponse(responseCode = "404", description = "El carrito no ha sido encontrado.")
    })
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(
            @Parameter(description = "ID del carrito") @PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @Operation(summary = "1) Agregar Producto al Carrito.", description = "Agrega la cantidad especificada de productos al carrito. Sí el producto ya eciste en el carrito, la cantidad es actualizada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El producto ha sido agregado."),
            @ApiResponse(responseCode = "404", description = "El carrito o el producto no existe")
    })
    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> addProductToCart  (
            @Parameter(description = "ID del carrito") @PathVariable Long cartId,
            @Parameter(description = "ID del producto") @PathVariable Long productId,
            @Parameter(description = "Cantidad ha agregar") @RequestParam int quantity)
            throws BusinessRuleException {
        return ResponseEntity.ok(cartService.addProductToCart(cartId, productId, quantity));
    }

    @Operation(summary = "3) Actualizar Cantidad.", description = "Actualiza la cantidad de productos, contenidos en un carrito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada"),
            @ApiResponse(responseCode = "404", description = "El carrito o el producto no existen")
    })
    @PutMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @Parameter(description = "ID del carrito") @PathVariable Long cartId,
            @Parameter(description = "ID del producto") @PathVariable Long productId,
            @Parameter(description = "Cantidad ha actualizar") @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateProductQuantity(cartId, productId, quantity));
    }

    @Operation(summary = "2) Eliminar Producto del Carrito.", description = "Elimina un determinado producto de un carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Carrito o producto no existen")
    })
    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> removeProductFromCart(
            @Parameter(description = "ID del carrito") @PathVariable Long cartId,
            @Parameter(description = "ID del producto") @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(cartId, productId));
    }

    /*
    @Operation(summary = "Clear all products in the cart", description = "Removes all products from the cart and sets the total to zero.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<CartDTO> clearCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.clearCart(cartId));
    }
    */
}
