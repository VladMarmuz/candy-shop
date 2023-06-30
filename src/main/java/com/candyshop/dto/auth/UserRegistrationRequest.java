package com.candyshop.dto.auth;

import com.candyshop.validation.OnCreate;
import com.candyshop.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationRequest {

    private Long id;

    @NotNull(message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Name length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Email must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Email length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    @Email
    private String email;
    private String password;

    @NotNull(message = "Phone number must be not null.")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$",
            message = "The phone should be like example (+375298535963)")
    private String phoneNumber;

}
