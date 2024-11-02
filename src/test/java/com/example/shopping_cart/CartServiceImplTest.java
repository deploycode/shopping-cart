package com.example.shopping_cart;

import com.example.shopping_cart.dtos.CartDTO;
import com.example.shopping_cart.entities.Cart;
import com.example.shopping_cart.entities.User;
import com.example.shopping_cart.exceptions.BusinessRuleException;
import com.example.shopping_cart.repositories.CartRepository;
import com.example.shopping_cart.repositories.UserRepository;
import com.example.shopping_cart.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    void createCart_ShouldCreateNewCart_WhenUserHasNoCart() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Cart savedCart = new Cart();
        savedCart.setId(1L);
        savedCart.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        CartDTO cartDTO = cartService.createCart(userId);

        // Assert
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getUserId()).isEqualTo(userId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void createCart_ShouldThrowException_WhenUserAlreadyHasCart() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Cart existingCart = new Cart();
        existingCart.setId(1L);
        existingCart.setUser(user);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));

        // Asserts
        assertThatThrownBy(() -> cartService.createCart(userId))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("El usuario ya tiene un carrito de compras asociado.")
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.PRECONDITION_FAILED);

        verify(cartRepository, never()).save(any(Cart.class));
    }
}
