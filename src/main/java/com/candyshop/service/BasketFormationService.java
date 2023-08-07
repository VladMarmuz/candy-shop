package com.candyshop.service;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductIntoBasket;


public interface BasketFormationService {

    Basket ifProductIsInTheBasket(ProductIntoBasketDto productIntoBasketDto,
                                  Basket basket,
                                  Product product,
                                  ProductIntoBasket productIntoBasket);

    Basket productIsNotInTheBasket(ProductIntoBasketDto productIntoBasketDto,
                                   Basket basket,
                                   Product product);
}
