package com.candyshop.controller;

import com.candyshop.dto.UserDto;
import com.candyshop.dto.auth.RefreshToken;
import com.candyshop.dto.auth.UserLoginRequest;
import com.candyshop.dto.auth.UserLoginResponse;
import com.candyshop.entity.User;
import com.candyshop.mappers.UserMapper;
import com.candyshop.service.AuthService;
import com.candyshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public UserLoginResponse login(@Validated @RequestBody UserLoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @Operation(summary = "Method for registration user")
    public UserDto register(@Validated @RequestBody UserDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public UserLoginResponse refresh(@RequestBody RefreshToken refreshToken) {
        return authService.refresh(refreshToken);
    }
}

