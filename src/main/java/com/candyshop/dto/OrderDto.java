package com.candyshop.dto;

import com.candyshop.entity.Product;
import com.candyshop.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Order DTO")
public class OrderDto {

    private Long id;

    private LocalDate date;

    @NotNull(message = "Address must be not null")
    private String address;

    private BigDecimal priceResult;

    private Status status;

    private List<Product> products;
}
