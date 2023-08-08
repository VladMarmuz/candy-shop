package com.candyshop.dto;

import com.candyshop.entity.ProductIntoBasket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BasketDto {

    @Schema(description = "Basket id", example = "1")
    private Long id;

    @Schema(description = "Final price for all selected products",
            example = "44.50")
    private BigDecimal priceResult;
    private List<ProductIntoBasket> products;

}
