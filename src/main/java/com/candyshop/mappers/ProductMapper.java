package com.candyshop.mappers;

import com.candyshop.dto.ProductDto;
import com.candyshop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends Mappable<Product, ProductDto> {
}
