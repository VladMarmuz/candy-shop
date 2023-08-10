package com.candyshop.service;

import com.candyshop.auth.JwtTokenManager;
import com.candyshop.dto.auth.Token;
import com.candyshop.dto.auth.UserLoginRequest;
import com.candyshop.dto.auth.UserLoginResponse;
import com.candyshop.entity.User;
import com.candyshop.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private final Long ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void testLoginSuccess() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        User user = new User();
        user.setId(ID);
        user.setName("Test User");
        when(userService.getByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtTokenManager.getToken(user)).thenReturn(getToken());

        UserLoginResponse loginResponse = authServiceImpl.login(loginRequest);

        assertEquals(user.getId(), loginResponse.getUserId());
        assertEquals(user.getName(), loginResponse.getName());
        assertEquals(getToken(), loginResponse.getToken());
    }

    @Test
    void testLoginInvalidCredentials() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        User user = new User();
        user.setId(ID);
        when(userService.getByEmail(loginRequest.getEmail())).thenReturn(user);
        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(RuntimeException.class,
                () -> authServiceImpl.login(loginRequest));
    }

    private Token getToken() {
        Token token = new Token();
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setExpirationIn(10L);
        return token;
    }

}