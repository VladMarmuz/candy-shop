package com.candyshop.service;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Order;

public interface CollectProductsService {

    Order collectProducts(OrderCreateDto orderCreateDto);

}
