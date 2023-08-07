package com.candyshop.service.impl;

import com.candyshop.entity.Product;
import com.candyshop.entity.ProductImage;
import com.candyshop.entity.enums.Balance;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.ProductRepository;
import com.candyshop.service.ImageService;
import com.candyshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public Product getProduct(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found"));
    }

    @Transactional
    public Product create(final Product product) {
        product.setBalance(Balance.IN_STOCK);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll(final PageRequest pageRequest) {
        Page<Product> page = productRepository.findAll(pageRequest);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException(
                    "There are doesn't have product in the db");
        }
        return page.getContent();
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        getProduct(productId);
        productRepository.deleteById(productId);
    }

    @Transactional
    public Product updateProduct(final Product product) {
        Product foundProduct = getProduct(product.getId());
        foundProduct.setName(product.getName());
        foundProduct.setBalance(product.getBalance());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setDescription(product.getDescription());
        return productRepository.save(foundProduct);
    }

    @Transactional
    public void uploadImage(final Long id, final ProductImage image) {
        Product product = getProduct(id);
        String fileName = imageService.upload(image);
        product.setImage(fileName);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByNameContaining(final String fragment) {
        List<Product> allProductsByFragment =
                productRepository.findAllByNameContainingIgnoreCase(fragment);

        return Optional.of(allProductsByFragment)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ResourceNotFoundException("Such kind of products "
                        + "don't exist in the db"));
    }
}
