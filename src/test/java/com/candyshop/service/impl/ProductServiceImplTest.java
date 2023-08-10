package com.candyshop.service.impl;

import com.candyshop.entity.Product;
import com.candyshop.entity.enums.Balance;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.ProductRepository;
import com.candyshop.service.ImageService;
import com.candyshop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ProductServiceImplTest {

    private final Long ID = 1L;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Test
    void testGetProduct_ValidId_ReturnsProduct() {
        Product mockProduct = new Product();
        mockProduct.setId(ID);
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(mockProduct));

        Product result = productServiceImpl.getProduct(ID);

        assertEquals(mockProduct, result);
        verify(productRepository, times(1)).findById(ID);
    }

    @Test
    void testGetProduct_InvalidId_ThrowsResourceNotFoundException() {
        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productServiceImpl.getProduct(ID));

        verify(productRepository, times(1)).findById(ID);
    }

    @Test
    void testCreate_ValidProduct_ReturnsCreatedProductWithInStockBalance() {
        Product productToCreate = getProduct();
        when(productServiceImpl.create(productToCreate))
                .thenReturn(productToCreate);

        Product createdProduct = productServiceImpl.create(productToCreate);

        assertNotNull(createdProduct.getId());
        assertEquals(productToCreate.getName(), createdProduct.getName());
        assertEquals(Balance.IN_STOCK, createdProduct.getBalance());
        assertEquals(productToCreate.getPrice(), createdProduct.getPrice());
        assertEquals(productToCreate.getDescription(),
                createdProduct.getDescription());
        verify(productRepository, times(1))
                .save(productToCreate);
    }

    @Test
    void testGetAll_ReturnsListOfProducts() {
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(getProduct());
        mockProducts.add(Product.builder()
                .id(2L)
                .name("Product 2")
                .balance(Balance.IN_STOCK)
                .description("Description 2")
                .price(BigDecimal.valueOf(20.0))
                .build());
        Page mockPage = mock(Page.class);
        when(mockPage.isEmpty()).thenReturn(false);
        when(mockPage.getContent()).thenReturn(mockProducts);
        when(productRepository.findAll(pageRequest)).thenReturn(mockPage);

        List<Product> result = productServiceImpl.getAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(mockProducts.size(), result.size());
        assertEquals(mockProducts.get(0), result.get(0));
        assertEquals(mockProducts.get(1), result.get(1));
        verify(productRepository, times(1))
                .findAll(pageRequest);
    }

    @Test
    void testGetAll_EmptyList_ThrowsResourceNotFoundException() {
        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Product> emptyPage = mock(Page.class);
        when(emptyPage.isEmpty()).thenReturn(true);
        when(productRepository.findAll(pageRequest)).thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class,
                () -> productServiceImpl.getAll(pageRequest));
        verify(productRepository, times(1))
                .findAll(pageRequest);
    }

    @Test
    void testDeleteProduct_ValidProductId_ProductDeletedSuccessfully() {
        Product mockProduct = getProduct();
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(mockProduct));

        productServiceImpl.deleteProduct(ID);

        verify(productRepository, times(1))
                .deleteById(ID);
    }

    @Test
    void testDeleteProduct_InvalidProductId_ThrowsResourceNotFoundException() {
        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productServiceImpl.deleteProduct(ID));
        verify(productRepository, times(0))
                .deleteById(ID);
    }

    @Test
    void testUpdateProduct_ValidProduct_ReturnsUpdatedProduct() {
        Product existingProduct = getProduct();
        Product updatedProduct = Product.builder()
                .id(ID)
                .name("Updated Product")
                .balance(Balance.IN_STOCK)
                .description("Updated description")
                .price(BigDecimal.valueOf(40.0))
                .build();
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result = productServiceImpl.updateProduct(updatedProduct);

        assertEquals(updatedProduct.getId(), result.getId());
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getBalance(), result.getBalance());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        verify(productRepository, times(1)).findById(ID);
        verify(productRepository,
                times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_InvalidProduct() {
        Product invalidProduct = getProduct();
        when(productRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productServiceImpl.updateProduct(invalidProduct));
        verify(productRepository,
                times(0)).save(invalidProduct);
    }

    Product getProduct() {
        return Product.builder()
                .id(ID)
                .name("Product")
                .balance(Balance.IN_STOCK)
                .description("product")
                .price(BigDecimal.valueOf(5.5))
                .build();
    }

}