package com.candyshop.service.impl;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.repository.ProductIntoBasketRepository;
import com.candyshop.service.BasketFormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BasketFormationServiceImpl implements BasketFormationService {

    private final ProductIntoBasketRepository productIntoBasketRepository;

    public Basket ifProductIsInTheBasket(ProductIntoBasketDto productIntoBasketDto,
                                         Basket basket,
                                         Product product,
                                         ProductIntoBasket productIntoBasket) {
        productIntoBasket.setNumberIntoBasket(
                productIntoBasket.getNumberIntoBasket() + productIntoBasketDto.getNumberOfAdded());
        productIntoBasket.setFinalPrice(productIntoBasket.getFinalPrice().add(product.getPrice()
                .multiply(new BigDecimal(productIntoBasketDto.getNumberOfAdded()))));
        productIntoBasketRepository.save(productIntoBasket);
        return setPriceResultToBasket(basket, productIntoBasketDto.getNumberOfAdded(), product.getPrice());


    }

    public Basket productIsNotInTheBasket(ProductIntoBasketDto productIntoBasketDto,
                                          Basket basket,
                                          Product product) {
        ProductIntoBasket productIfDoesNotExistsIntoDb = getProductIntoBasket(productIntoBasketDto, product);
        basket.getProducts().add(productIfDoesNotExistsIntoDb);

        if (basket.getPriceResult() != null) {
            basket.setPriceResult(basket.getPriceResult().add(productIfDoesNotExistsIntoDb.getFinalPrice()));
        } else {
            basket.setPriceResult(productIfDoesNotExistsIntoDb.getFinalPrice());
        }
        return basket;
    }


    public Basket setPriceResultToBasket(final Basket currentBasket, final int numberOfAdded,
                                         final BigDecimal price) {
        currentBasket.setPriceResult(currentBasket.getPriceResult()
                .add(price.multiply(new BigDecimal(numberOfAdded))));
        return currentBasket;
    }

    public ProductIntoBasket getProductIntoBasket(final ProductIntoBasketDto productIntoBasketDto,
                                                  final Product currentProduct) {
        return ProductIntoBasket.builder()
                .name(currentProduct.getName())
                .price(currentProduct.getPrice())
                .numberIntoBasket(productIntoBasketDto.getNumberOfAdded())
                .finalPrice(currentProduct.getPrice()
                        .multiply(new BigDecimal(productIntoBasketDto.getNumberOfAdded())))
                .build();
    }

}
