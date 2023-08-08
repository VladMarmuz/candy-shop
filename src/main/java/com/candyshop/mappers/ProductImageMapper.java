package com.candyshop.mappers;

import com.candyshop.dto.ProductImageDto;
import com.candyshop.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductImageMapper extends Mappable<ProductImage, ProductImageDto> {
}
