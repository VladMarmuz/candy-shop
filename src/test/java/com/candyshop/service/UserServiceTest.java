package com.candyshop.service;

import com.candyshop.entity.Basket;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {
    private final Long ID = 1L;
    private final String testEmail = "test@example.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetById_PositiveScenario() {
        User user = new User();
        user.setId(ID);

        when(userRepository.findUserById(ID)).thenReturn(Optional.of(user));

        User result = userService.getById(ID);

        assertEquals(ID, result.getId());
        verify(userRepository, times(1)).findUserById(ID);
    }

    @Test
    void testGetById_NegativeScenario() {

        when(userRepository.findUserById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(ID));
        verify(userRepository, times(1)).findUserById(ID);
    }

    @Test
    void testGetByEmail_UserFound() {
        User user = new User();
        user.setEmail(testEmail);

        when(userRepository.findUserByEmail(testEmail))
                .thenReturn(Optional.of(user));

        User result = userService.getByEmail(testEmail);

        assertNotNull(result);
        assertEquals(testEmail, result.getEmail());

        verify(userRepository, times(1))
                .findUserByEmail(testEmail);
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(userRepository.findUserByEmail(testEmail))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail(testEmail));

        verify(userRepository, times(1))
                .findUserByEmail(testEmail);
    }

    @Test
    void testGetByEmail_NullEmail() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail(null));

        verify(userRepository, times(1)).findUserByEmail(any());
    }

    @Test
    void testCreateUser_Successful() {
        User user = new User();
        user.setEmail(testEmail);
        user.setPassword("testPassword");

        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("encodedPassword");
        when(basketRepository.save(any(Basket.class))).thenReturn(new Basket());

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(Role.ROLE_USER, createdUser.getRole());
        assertFalse(createdUser.isEnabled());
        assertNotNull(createdUser.getBasket());
        verify(userRepository, times(1)).save(user);
        verify(basketRepository, times(1))
                .save(any(Basket.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail(testEmail);
        existingUser.setPassword("testPassword");

        when(userRepository.findUserByEmail(
                existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalStateException.class,
                () -> userService.create(existingUser));
        verify(userRepository, never()).save(existingUser);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void testCreateUser_AssociateBasketWithUser() {
        User user = new User();
        user.setEmail(testEmail);
        user.setPassword("testPassword");

        when(userRepository.findUserByEmail(user.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("encodedPassword");

        when(basketRepository.save(Mockito.any(Basket.class)))
                .thenAnswer(invocation -> {
                    Basket savedBasket = invocation.getArgument(0);
                    savedBasket.setId(1L);
                    return savedBasket;
                });

        User createdUser = userService.create(user);

        assertNotNull(createdUser.getBasket());
    }


}