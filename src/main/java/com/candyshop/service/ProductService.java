package com.candyshop.service;

import com.candyshop.entity.Product;
import com.candyshop.entity.enums.Balance;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Transactional
    public Product create(Product product){
        product.setBalance(Balance.IN_STOCK);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()){
            throw new ResourceNotFoundException("There are doesn't have product in the db");
        }
        return allProducts;
    }

    @Transactional
    public void deleteProduct(Long productId) {
        getProduct(productId);
        productRepository.deleteById(productId);
    }

    @Transactional
    public Product updateProduct(Product product) {
        Product foundProduct = getProduct(product.getId());
        foundProduct.setName(product.getName());
        foundProduct.setBalance(product.getBalance());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setDescription(product.getDescription());
        return productRepository.save(foundProduct);
    }
}
