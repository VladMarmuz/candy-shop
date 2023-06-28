package com.candyshop.dto;

import com.candyshop.entity.enums.Balance;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductDto {

    private Long id;

    @NotNull(message = "Name must be not null")
    private String name;

    @NotNull(message = "Description must be not null")
    private String description;

    @NotNull(message = "Price must be not null")
    private BigDecimal price;

    private Balance balance;


}
