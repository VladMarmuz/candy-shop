package com.candyshop.service;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrdersByUserId(Long userId);

    Order create(OrderCreateDto orderCreateDto);

    void delete(Long orderId);

    List<Order> getOrdersSortedByDate();
}
