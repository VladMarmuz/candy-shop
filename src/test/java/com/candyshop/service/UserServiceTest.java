package com.candyshop.service;

import com.candyshop.entity.Basket;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
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
        verify(userRepository, times(1))
                .findUserById(ID);
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
        when(userRepository.findUserByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail(null));

        verify(userRepository, times(1))
                .findUserByEmail(any());
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

        when(userRepository.findUserByEmail(existingUser.getEmail()))
                .thenReturn(Optional.of(existingUser));

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

    @Test
    void testGetAllUsers_Successful() {
        List<User> expectedUsers = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAll();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void testGetAll_WithEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getAll());
    }

    @Test
    void testGetAll_WithNullList() {
        when(userRepository.findAll()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> userService.getAll());
    }

    @Test
    void testDeleteUser_Transaction() {
        User user = new User();
        user.setId(ID);
        when(userRepository.findUserById(ID)).thenReturn(Optional.of(user));

        userService.deleteUser(ID);

        InOrder inOrder = inOrder(userRepository, basketRepository);
        inOrder.verify(userRepository).findUserById(ID);
        inOrder.verify(userRepository).deleteById(ID);
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        user.setId(ID);
        when(userRepository.findUserById(ID)).thenReturn(Optional.of(user));

        userService.deleteUser(ID);

        verify(userRepository, times(1)).findUserById(ID);
        verify(userRepository, times(1)).deleteById(ID);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findUserById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(ID));
        verify(userRepository, times(1)).findUserById(ID);
        verify(userRepository, never()).deleteById(ID);
    }

    @Test
    void testUpdateUserDetails_UserNotFound() {
        User updatedUser = getUpdatedUser("John Doe", "john.doe@example.com", "newpassword", Role.ROLE_ADMIN);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.update(ID, updatedUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserDetails_ValidUserId() {
        User existingUser = getUpdatedUser("John",
                "john@example.com", "123456", Role.ROLE_USER);

        User updatedUser = getUpdatedUser("John Doe",
                "john.doe@example.com", "newpassword",
                Role.ROLE_ADMIN);

        when(userRepository.findUserById(ID))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(updatedUser);
        when(passwordEncoder.encode(updatedUser.getPassword()))
                .thenReturn("encodedPassword");

        User result = userService.update(ID, updatedUser);

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals("newpassword", result.getPassword());
        assertEquals(updatedUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedUser.getRole(), result.getRole());

        verify(userRepository, times(1)).findUserById(ID);
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1))
                .encode(updatedUser.getPassword());
    }

    private User getUpdatedUser(String John_Doe,
                                String email,
                                String password,
                                Role roleAdmin) {
        return User.builder()
                .id(ID)
                .name(John_Doe)
                .email(email)
                .password(password)
                .role(roleAdmin)
                .build();
    }


}