package com.candyshop.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotNull(message = "Email must be not null")
    private String email;
}
