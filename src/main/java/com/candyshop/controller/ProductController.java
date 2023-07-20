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
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ProductDto getProduct(@PathVariable final Long id) {
        Product product = productService.getProduct(id);
        return productMapper.toDto(product);
    }

    @GetMapping("/")
    @Operation(summary = "Get all product")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size
    ) {
        List<Product> currentProducts =
                productService.getAll(PageRequest.of(page, size));
        return currentProducts.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{fragment}")
    @Operation(summary = "Get products by some letters")
    public List<ProductDto> getProductsBySomeLetters(
            @PathVariable String fragment) {
        List<Product> currentProducts =
                productService.getProductsByNameContaining(fragment);
        return productMapper.toDto(currentProducts);
    }


    @PostMapping("/create")
    @Operation(summary = "Create product")
    public ProductDto create(@Validated
                             @RequestBody final ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product createdProduct = productService.create(product);
        return productMapper.toDto(createdProduct);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product")
    public String delete(@PathVariable final Long productId) {
        productService.deleteProduct(productId);
        return "Product successfully deleted";
    }

    @PutMapping("/")
    @Operation(summary = "Update product")
    public ProductDto update(@Validated
                             @RequestBody final ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product currentProduct = productService.updateProduct(product);
        return productMapper.toDto(currentProduct);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to product")
    public void uploadImage(
            @PathVariable final Long id,
            @Validated
            @ModelAttribute final ProductImageDto productImage) {
        ProductImage image = productImageMapper.toEntity(productImage);
        productService.uploadImage(id, image);
    }
}
