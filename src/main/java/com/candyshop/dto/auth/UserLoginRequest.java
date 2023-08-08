package com.candyshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data for login request")
public class UserLoginRequest {

    @Schema(description = "User email", example = "petrov@gmail.com")
    @NotNull(message = "Email must be not null")
    private String email;

    @Schema(description = "User password", example = "11petr11")
    private String password;

}
