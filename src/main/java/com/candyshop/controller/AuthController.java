package com.candyshop.controller;

import com.candyshop.dto.auth.RefreshToken;
import com.candyshop.dto.auth.UserLoginRequest;
import com.candyshop.dto.auth.UserLoginResponse;
import com.candyshop.dto.auth.UserRegistrationRequest;
import com.candyshop.entity.User;
import com.candyshop.mappers.UserMapper;
import com.candyshop.service.AuthService;
import com.candyshop.service.UserService;
import com.candyshop.validation.OnCreate;
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
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public UserLoginResponse login(@Validated @RequestBody final UserLoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserRegistrationRequest register(@Validated @RequestBody UserRegistrationRequest userRegistrationRequest) {
        User user = userMapper.toEntity(userRegistrationRequest);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public UserLoginResponse refresh(@RequestBody RefreshToken refreshToken) {
        return authService.refresh(refreshToken);
    }
}

