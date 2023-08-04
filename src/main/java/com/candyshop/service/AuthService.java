package com.candyshop.service;

import com.candyshop.dto.auth.RefreshToken;
import com.candyshop.dto.auth.UserLoginRequest;
import com.candyshop.dto.auth.UserLoginResponse;

public interface AuthService {

    UserLoginResponse login(UserLoginRequest loginRequest);

    UserLoginResponse refresh(final RefreshToken refreshToken);
}
