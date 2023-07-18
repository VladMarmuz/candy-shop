package com.candyshop.service;

import com.candyshop.auth.JwtTokenManager;
import com.candyshop.dto.auth.RefreshToken;
import com.candyshop.dto.auth.UserLoginRequest;
import com.candyshop.dto.auth.UserLoginResponse;
import com.candyshop.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponse login(final UserLoginRequest loginRequest) {
        User currentUser = userService.getByEmail(loginRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        return getLoginResponse(currentUser);
    }

    public UserLoginResponse refresh(final RefreshToken refreshToken) {
        return jwtTokenManager.refreshUserTokens(
                refreshToken.getRefreshToken(),
                refreshToken.getUserId());
    }

    private UserLoginResponse getLoginResponse(final User currentUser) {
        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setUserId(currentUser.getId());
        loginResponse.setName(currentUser.getName());
        loginResponse.setToken(jwtTokenManager.getToken(currentUser));
        return loginResponse;
    }
}
