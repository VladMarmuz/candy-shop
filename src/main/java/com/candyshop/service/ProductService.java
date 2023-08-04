package com.candyshop.service;

import com.candyshop.entity.Product;
import com.candyshop.entity.ProductImage;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    Product create(Product product);

    List<Product> getAll(PageRequest pageRequest);

    void deleteProduct(Long productId);

    Product updateProduct(Product product);

    void uploadImage(Long id, ProductImage image);

    List<Product> getProductsByNameContaining(String fragment);
}
