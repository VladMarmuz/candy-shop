package com.candyshop.service;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;

public interface BasketService {

    Basket getBasketByUserId(Long userId);

    void addProduct(ProductIntoBasketDto productIntoBasketDto);

    void deleteProductFromBasket(Long productId);

    void deleteAllProductFromBasket(Long basketId);

}
