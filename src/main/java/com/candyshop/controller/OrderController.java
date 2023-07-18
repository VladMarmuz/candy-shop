package com.candyshop.controller;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.dto.OrderDto;
import com.candyshop.entity.Order;
import com.candyshop.mappers.OrderMapper;
import com.candyshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<OrderDto> getOrders(@PathVariable Long userId) {
        List<Order> foundOrders = orderService.getOrdersByUserId(userId);
        return foundOrders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @PostMapping("/")
    @Operation(summary = "Create Order")
    public OrderDto createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        Order order = orderService.create(orderCreateDto);
        return orderMapper.toDto(order);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return "Order successfully deleted";
    }
}
