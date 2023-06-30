package com.candyshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data for response after login")
public class UserLoginResponse {

    private Long userId;
    private String name;
    private Token token;
}
