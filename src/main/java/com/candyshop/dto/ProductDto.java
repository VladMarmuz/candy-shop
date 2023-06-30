package com.candyshop.dto;

import com.candyshop.entity.enums.Balance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Product DTO")
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
