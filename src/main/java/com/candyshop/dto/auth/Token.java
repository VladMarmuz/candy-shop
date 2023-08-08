package com.candyshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JWT Tokens")
public class Token {

    private String accessToken;
    private long expirationIn;
    private String refreshToken;

}
