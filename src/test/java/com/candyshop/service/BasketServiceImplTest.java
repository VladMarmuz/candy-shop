package com.candyshop.service;

import com.candyshop.dto.ProductIntoBasketDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.entity.enums.Balance;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.BasketRepository;
import com.candyshop.repository.ProductIntoBasketRepository;
import com.candyshop.service.impl.BasketServiceImpl;
import com.candyshop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {
    private final Long ID = 1L;

    @Mock
    private BasketRepository basketRepository;
    @Mock
    private ProductServiceImpl productServiceImpl;
    @Mock
    private ProductIntoBasketRepository productIntoBasketRepository;
    @InjectMocks
    private BasketServiceImpl basketServiceImpl;

    @Test
    void testGetBasketByUserId_Ok() {
        Basket expectedBasket = new Basket();
        when(basketRepository.findBasketByUserId(ID))
                .thenReturn(expectedBasket);

        Basket resultBasket = basketServiceImpl.getBasketByUserId(ID);

        assertNotNull(resultBasket);
        assertEquals(expectedBasket, resultBasket);
    }

    @Test
    void testGetBasketByUserId_ThrowException() {
        when(basketRepository.findBasketByUserId(ID))
                .thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(RuntimeException.class,
                () -> basketServiceImpl.getBasketByUserId(ID));
    }

    @Test
    void addProduct_ProductExistsInBasket() {
        ProductIntoBasketDto productIntoBasketDto = getProductIntoBasketDto();
        Product currentProduct = getProduct();
        ProductIntoBasket existingProductIntoBasket = getProductIntoBasket();
        Basket currentBasket = getBasket(existingProductIntoBasket);

        when(productServiceImpl.getProduct(ID)).thenReturn(currentProduct);
        when(basketRepository.findBasketByUserId(ID)).thenReturn(currentBasket);
        when(productIntoBasketRepository.findProductIntoBasketByName(currentProduct.getName()))
                .thenReturn(Optional.of(existingProductIntoBasket));

        basketServiceImpl.addProduct(productIntoBasketDto);

        verify(productIntoBasketRepository, times(1)).save(existingProductIntoBasket);
        verify(basketRepository, times(1)).save(currentBasket);
        assertEquals(7, existingProductIntoBasket.getNumberIntoBasket());
        assertEquals(BigDecimal.valueOf(120), existingProductIntoBasket.getFinalPrice());
    }

    @Test
    void testDeleteProductFromBasket_ProductFound_ShouldUpdateBasketPrice() {
        ProductIntoBasket productIntoBasket = getProductIntoBasket();
        Basket basket = getBasket(productIntoBasket);
        when(productIntoBasketRepository.findById(ID)).thenReturn(Optional.of(productIntoBasket));
        when(basketRepository.findBasketByProductId(ID)).thenReturn(basket);

        basketServiceImpl.deleteProductFromBasket(ID);

        assertEquals(BigDecimal.valueOf(0L), basket.getPriceResult());
        verify(productIntoBasketRepository, times(1)).deleteProductIntoBasketById(ID);
    }

    @Test
    void testDeleteProductFromBasket_ProductNotFound_ShouldNotUpdateBasketPrice() {

        ProductIntoBasket productIntoBasket = getProductIntoBasket();
        Basket basket = getBasket(productIntoBasket);

        when(productIntoBasketRepository.findById(ID)).thenReturn(Optional.empty());

        basketServiceImpl.deleteProductFromBasket(ID);

        assertEquals(BigDecimal.valueOf(100L), basket.getPriceResult());
    }

    @Test
    void testDeleteAllProductFromBasket_BasketEmpty_ShouldThrowException() {
        when(productIntoBasketRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> basketServiceImpl.deleteAllProductFromBasket(ID));
        verify(productIntoBasketRepository, never()).deleteAllProductFromBasket(ID);
        verify(basketRepository, never()).save(any());
    }


    private Product getProduct() {
        Product currentProduct = new Product();
        currentProduct.setId(ID);
        currentProduct.setName("Test Product");
        currentProduct.setPrice(BigDecimal.valueOf(10));
        currentProduct.setBalance(Balance.IN_STOCK);
        return currentProduct;
    }

    private Basket getBasket(ProductIntoBasket productIntoBasket) {
        return Basket.builder()
                .id(ID)
                .priceResult(BigDecimal.valueOf(100))
                .products(List.of(productIntoBasket))
                .build();
    }

    private ProductIntoBasketDto getProductIntoBasketDto() {
        ProductIntoBasketDto productIntoBasketDto =
                new ProductIntoBasketDto();
        productIntoBasketDto.setUserId(ID);
        productIntoBasketDto.setProductId(ID);
        productIntoBasketDto.setNumberOfAdded(2);
        return productIntoBasketDto;
    }

    private ProductIntoBasket getProductIntoBasket() {
        return ProductIntoBasket.builder()
                .id(ID)
                .finalPrice(BigDecimal.valueOf(100))
                .price(BigDecimal.valueOf(20))
                .numberIntoBasket(5)
                .build();
    }
}