package com.candyshop.dto;

import com.candyshop.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class BasketDto {

    private Long id;

    private List<Product> products;
}
