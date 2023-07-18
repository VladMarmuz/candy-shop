package com.candyshop.dto;

import com.candyshop.entity.enums.Balance;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Product create DTO")
public class ProductDto {

    @Schema(description = "Product id", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Box")
    @NotNull(message = "Name must be not null")
    private String name;

    @Schema(description = "Product description",
            example = "Box for cakes 24x24x30")
    @NotNull(message = "Description must be not null")
    private String description;

    @Schema(description = "Product price", example = "Price for 1 box")
    @NotNull(message = "Price must be not null")
    private BigDecimal price;

    @Schema(description = "Balance field", example = "IN_STOCK")
    private Balance balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;
}
