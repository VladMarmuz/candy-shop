package com.candyshop.dto.auth;

import lombok.Data;

@Data
public class UserLoginResponse {

    private Long userId;
    private String name;
    private Token token;
}
