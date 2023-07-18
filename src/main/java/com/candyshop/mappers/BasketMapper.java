package com.candyshop.mappers;

import com.candyshop.dto.BasketDto;
import com.candyshop.entity.Basket;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BasketMapper extends Mappable<Basket, BasketDto> {

}
