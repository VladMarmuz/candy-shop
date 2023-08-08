package com.candyshop.service.impl;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.ProductIntoBasketRepository;
import com.candyshop.service.BasketFormationService;
import com.candyshop.service.BasketService;
import com.candyshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final ProductIntoBasketRepository productIntoBasketRepository;
    private final BasketFormationService basketFormationService;

    @Transactional(readOnly = true)
    public Basket getBasketByUserId(final Long userId) {
        return basketRepository.findBasketByUserId(userId);
    }

    @Transactional
    public void addProduct(final ProductIntoBasketDto productIntoBasketDto) {
        Basket currentBasket = getBasketByUserId(productIntoBasketDto.getUserId());
        Product currentProduct = productService.getProduct(productIntoBasketDto.getProductId());
        Optional<ProductIntoBasket> currentProductIntoBasket =
                productIntoBasketRepository.findProductIntoBasketByName(currentProduct.getName());
        Basket basket;
        if (currentProductIntoBasket.isPresent()) {
            basket = basketFormationService.ifProductIsInTheBasket(productIntoBasketDto, currentBasket,
                    currentProduct, currentProductIntoBasket.get());
        } else {
            basket = basketFormationService.productIsNotInTheBasket(productIntoBasketDto, currentBasket, currentProduct);
        }
        basketRepository.save(basket);
    }

    @Transactional
    public void deleteProductFromBasket(final Long productId) {
        Optional<ProductIntoBasket> productIntoBasket = productIntoBasketRepository.findById(productId);
        if (productIntoBasket.isPresent()) {
            Basket basket = basketRepository.findBasketByProductId(productId);
            basket.setPriceResult(basket.getPriceResult()
                    .subtract(productIntoBasket.get().getFinalPrice()));
        }
        productIntoBasketRepository.deleteProductIntoBasketById(productId);
    }

    @Transactional
    public void deleteAllProductFromBasket(final Long basketId) {
        int deletedProductsCount = productIntoBasketRepository.deleteAllProductFromBasket(basketId);
        if (deletedProductsCount == 0) {
            throw new ResourceNotFoundException("There are not products in the basket");
        }
        removeCountOfPriceResult(basketId);
    }

    public void removeCountOfPriceResult(final Long basketId) {
        Optional<Basket> basket = basketRepository.findById(basketId);
        Basket currentBasket = basket.get();
        currentBasket.setPriceResult(null);
        basketRepository.save(currentBasket);
    }

}
