package com.candyshop.service.impl;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Order;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.OrderRepository;
import com.candyshop.service.CollectProductsService;
import com.candyshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CollectProductsService collectProductsService;

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(final Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Transactional
    public Order create(final OrderCreateDto orderCreateDto) {
        Order buildingOrder = collectProductsService.collectProducts(orderCreateDto);
        return orderRepository.save(buildingOrder);
    }

    @Transactional
    public void delete(final Long orderId) {
        Optional<Order> currentOrder = orderRepository.findById(orderId);
        currentOrder.ifPresentOrElse(order -> orderRepository.deleteById(orderId),
                () -> {
                    throw new ResourceNotFoundException("Order doesn't exists");
                });
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersSortedByDate() {
        List<Order> orderList = orderRepository.findAllByOrderByDateAsc();
        if (orderList.isEmpty()) {
            throw new ResourceNotFoundException("Orders don't exists");
        }
        return orderList;
    }

}
