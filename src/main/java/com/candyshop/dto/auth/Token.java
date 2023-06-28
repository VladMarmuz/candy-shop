package com.candyshop.dto.auth;

import lombok.Data;

@Data
public class Token {

    private String accessToken;
    private long expirationIn;
    private String refreshToken;
}
