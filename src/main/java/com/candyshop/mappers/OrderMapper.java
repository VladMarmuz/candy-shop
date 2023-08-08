package com.candyshop.mappers;

import com.candyshop.dto.OrderDto;
import com.candyshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends Mappable<Order, OrderDto> {
}
