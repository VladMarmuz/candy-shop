package com.candyshop.controller;

import com.candyshop.dto.ProductDto;
import com.candyshop.dto.ProductImageDto;
import com.candyshop.entity.Product;
import com.candyshop.entity.ProductImage;
import com.candyshop.mappers.ProductImageMapper;
import com.candyshop.mappers.ProductMapper;
import com.candyshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "Product API")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get product by productId")
    public ProductDto getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return productMapper.toDto(product);
    }

    @GetMapping("/")
    @Operation(summary = "Get all product")
    public List<ProductDto> getAllProducts() {
        List<Product> currentUsers = productService.getAll();
        return currentUsers.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @PostMapping("/create")
    @Operation(summary = "Create product")
    public ProductDto create(@Validated @RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product createdProduct = productService.create(product);
        return productMapper.toDto(createdProduct);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product")
    public String delete(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "Product successfully deleted";
    }

    @PutMapping("/")
    @Operation(summary = "Update product")
    public ProductDto update(@Validated @RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product currentProduct = productService.updateProduct(product);
        return productMapper.toDto(currentProduct);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to product")
    public void uploadImage(@PathVariable Long id,
                            @Validated @ModelAttribute ProductImageDto productImage) {
        ProductImage image = productImageMapper.toEntity(productImage);
        productService.uploadImage(id, image);
    }
}
