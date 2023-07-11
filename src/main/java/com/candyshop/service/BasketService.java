package com.candyshop.service;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.ProductIntoBasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;
    private final ProductIntoBasketRepository productIntoBasketRepository;

    @Transactional(readOnly = true)
    public Basket getBasketByUserId(Long userId) {
        Basket currentBasket = basketRepository.findBasketByUserId(userId);
        return currentBasket;
    }

    @Transactional
    public void addProduct(ProductIntoBasketDto productIntoBasketDto) {
        Basket currentBasket = getBasketByUserId(productIntoBasketDto.getUserId());
        Product currentProduct = productService.getProduct(productIntoBasketDto.getProductId());

        Optional<ProductIntoBasket> currentProductIntoBasket = productIntoBasketRepository.
                findProductIntoBasketByName(currentProduct.getName());

        if (currentProductIntoBasket.isPresent()) {
            ProductIntoBasket productIntoBasket = currentProductIntoBasket.get();
            productIntoBasket.setNumberIntoBasket(productIntoBasket.getNumberIntoBasket() +
                    productIntoBasketDto.getNumberOfAdded());
            productIntoBasket.setFinalPrice(productIntoBasket.getFinalPrice().add(currentProduct.getPrice()
                    .multiply(new BigDecimal(productIntoBasketDto.getNumberOfAdded()))));
            productIntoBasketRepository.save(productIntoBasket);
            setPriceResultToBasket(currentBasket, productIntoBasketDto.getNumberOfAdded(), currentProduct.getPrice());
        } else {
            ProductIntoBasket productIntoBasketIfProductDoesntExistsIntoDb =
                    getProductIntoBasket(productIntoBasketDto, currentProduct);
            currentBasket.getProducts().add(productIntoBasketIfProductDoesntExistsIntoDb);
            if(currentBasket.getPriceResult() != null) {
                currentBasket.setPriceResult(currentBasket.getPriceResult()
                        .add(productIntoBasketIfProductDoesntExistsIntoDb.getFinalPrice()));
            }else{
                currentBasket.setPriceResult(productIntoBasketIfProductDoesntExistsIntoDb.getFinalPrice());
            }
        }
        basketRepository.save(currentBasket);
    }

    private void setPriceResultToBasket(Basket currentBasket, int numberOfAdded, BigDecimal price) {
        currentBasket.setPriceResult(currentBasket.getPriceResult()
                .add(price.multiply(new BigDecimal(numberOfAdded))));
    }

    public ProductIntoBasket getProductIntoBasket(ProductIntoBasketDto productIntoBasketDto, Product currentProduct) {
        return ProductIntoBasket.builder()
                .name(currentProduct.getName())
                .price(currentProduct.getPrice())
                .numberIntoBasket(productIntoBasketDto.getNumberOfAdded())
                .finalPrice(currentProduct.getPrice()
                        .multiply(new BigDecimal(productIntoBasketDto.getNumberOfAdded())))
                .build();
    }

    @Transactional
    public void deleteProductFromBasket(Long productId) {
        Optional<ProductIntoBasket> productIntoBasket = productIntoBasketRepository.findById(productId);
        if (productIntoBasket.isPresent()) {
            Basket basket = basketRepository.findBasketByProductId(productId);
            basket.setPriceResult(basket.getPriceResult().subtract(productIntoBasket.get().getFinalPrice()));
        }
        productIntoBasketRepository.deleteProductIntoBasketById(productId);
    }

    @Transactional
    public void deleteAllProductFromBasket(Long basketId) {
        List<ProductIntoBasket> allProductsIntoBasket = productIntoBasketRepository.findAll();
        if (allProductsIntoBasket.isEmpty()) {
            throw new ResourceNotFoundException("There are not products in the basket");
        }
        productIntoBasketRepository.deleteAllProductFromBasket(basketId);

        removeCountOfPriceResult(basketId);
    }

    @Transactional
    public void removeCountOfPriceResult(Long basketId) {
        Optional<Basket> basket = basketRepository.findById(basketId);
        Basket currentBasket = basket.get();
        currentBasket.setPriceResult(null);
        basketRepository.save(currentBasket);
    }
}
