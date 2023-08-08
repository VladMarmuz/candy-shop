package com.candyshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Product add into basket DTO")
public class ProductIntoBasketDto {

    @Schema(description = "Product name", example = "Box")
    private Long userId;

    @Schema(description = "Product name", example = "Box")
    private Long productId;

    @Schema(description = "Number of product which need to add", example = "3")
    private int numberOfAdded;

}
