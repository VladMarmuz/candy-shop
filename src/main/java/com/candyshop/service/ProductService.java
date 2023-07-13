package com.candyshop.service;

import com.candyshop.entity.Product;
import com.candyshop.entity.enums.Balance;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "ProductService::getProduct", key = "#id")
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Transactional
    @Cacheable(value = "ProductService::getProduct", key = "#product.id")
    public Product create(Product product) {
        product.setBalance(Balance.IN_STOCK);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            throw new ResourceNotFoundException("There are doesn't have product in the db");
        }
        return allProducts;
    }

    @Transactional
    @CacheEvict(value = "ProductService::getProduct", key = "#productId")
    public void deleteProduct(Long productId) {
        getProduct(productId);
        productRepository.deleteById(productId);
    }

    @Transactional
    @CachePut(value = "ProductService::getProduct", key = "#product.id")
    public Product updateProduct(Product product) {
        Product foundProduct = getProduct(product.getId());
        foundProduct.setName(product.getName());
        foundProduct.setBalance(product.getBalance());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setDescription(product.getDescription());
        return productRepository.save(foundProduct);
    }
}
