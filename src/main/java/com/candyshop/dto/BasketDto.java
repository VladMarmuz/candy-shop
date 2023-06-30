package com.candyshop.dto;

import com.candyshop.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Basket DTO")
public class BasketDto {

    private Long id;

    private List<Product> products;
}
