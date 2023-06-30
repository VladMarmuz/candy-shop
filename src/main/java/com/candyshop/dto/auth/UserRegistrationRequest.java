package com.candyshop.dto.auth;

import com.candyshop.validation.OnCreate;
import com.candyshop.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "User DTO")
public class UserRegistrationRequest {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "User name", example = "Egor Petrov")
    @NotNull(message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Name length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "User email", example = "petrov@gmail.com")
    @NotNull(message = "Email must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Email length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    @Email
    private String email;

    @Schema(description = "User password", example = "11petr11")
    private String password;

    @Schema(description = "User phone number", example = "+375297768847")
    @NotNull(message = "Phone number must be not null.")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$",
            message = "The phone should be like example (+375298535963)")
    private String phoneNumber;

}
