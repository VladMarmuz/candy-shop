package com.candyshop.controller;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.dto.BasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.mappers.BasketMapper;
import com.candyshop.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/baskets")
@Tag(name = "Basket Controller", description = "Basket API")
public class BasketController {

    private final BasketService basketService;
    private final BasketMapper basketMapper;

    @GetMapping("/{userId}")
    @Operation(summary = "Get basket by UserID")
    public BasketDto getBasket(@PathVariable final Long userId) {
        Basket currentBasket = basketService.getBasketByUserId(userId);
        return basketMapper.toDto(currentBasket);
    }

    @PostMapping("/")
    @Operation(summary = "Add product in the basket")
    public String addProduct(@RequestBody final ProductIntoBasketDto productIntoBasketDto) {
        basketService.addProduct(productIntoBasketDto);
        return "Product successfully added in the basket";
    }

    @DeleteMapping("/{productIntoBasketId}")
    @Operation(summary = "Delete product from basket")
    public String deleteProductFromBasket(@PathVariable final Long productIntoBasketId) {
        basketService.deleteProductFromBasket(productIntoBasketId);
        return "Product was successfully deleted";
    }

    @DeleteMapping("/all/{basketId}")
    @Operation(summary = "Delete all products from basket")
    public String deleteAllProductFromBasket(@PathVariable final Long basketId) {
        basketService.deleteAllProductFromBasket(basketId);
        return "Products were successfully deleted";
    }

}
