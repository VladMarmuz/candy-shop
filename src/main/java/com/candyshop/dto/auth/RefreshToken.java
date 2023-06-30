package com.candyshop.dto.auth;

import lombok.Getter;

@Getter
public class RefreshToken {

    private Long userId;
    private String refreshToken;
}
