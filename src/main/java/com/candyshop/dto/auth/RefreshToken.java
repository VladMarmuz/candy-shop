package com.candyshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Data for refresh login")
public class RefreshToken {

    private Long userId;
    private String refreshToken;

}
