package com.candyshop.controller;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.dto.OrderDto;
import com.candyshop.entity.Order;
import com.candyshop.mappers.OrderMapper;
import com.candyshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Controller", description = "Order API")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/{userId}")
    @Operation(summary = "Get orders by user id")
    public List<OrderDto> getOrders(@PathVariable final Long userId) {
        List<Order> foundOrders = orderService.getOrdersByUserId(userId);
        return foundOrders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @GetMapping("/")
    @Operation(summary = "Get all orders sorted by date")
    public List<OrderDto> getOrders() {
        List<Order> foundOrders = orderService.getOrdersSortedByDate();
        return orderMapper.toDto(foundOrders);
    }

    @PostMapping("/")
    @Operation(summary = "Create Order")
    public OrderDto createOrder(@RequestBody final OrderCreateDto orderCreateDto) {
        Order order = orderService.create(orderCreateDto);
        return orderMapper.toDto(order);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order")
    public String deleteOrder(@PathVariable final Long orderId) {
        orderService.delete(orderId);
        return "Order successfully deleted";
    }
}
