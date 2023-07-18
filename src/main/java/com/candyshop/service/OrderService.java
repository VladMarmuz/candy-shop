package com.candyshop.service;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Order;
import com.candyshop.entity.ProductOrder;
import com.candyshop.entity.enums.Status;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BasketService basketService;
    private final UserService userService;
    private final ProductOrderRepository productOrderRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "OrderService::getOrdersByUserId", key = "#userId")
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Transactional
    @Cacheable(value = "OrderService::getOrdersByUserId", key = "#orderCreateDto.userId")
    public Order create(OrderCreateDto orderCreateDto) {
        Basket basket = basketService.getBasketByUserId(orderCreateDto.getUserId());
        if (basket.getProducts().isEmpty()) {
            throw new ResourceNotFoundException("There are not products in the basket");
        }
        Order createdOrder = orderRepository.save(buildOrder(orderCreateDto, basket));
        basketService.deleteAllProductFromBasket(basket.getId());
        return createdOrder;
    }

    private Order buildOrder(OrderCreateDto orderCreateDto, Basket basket) {
        return Order.builder()
                .user(userService.getById(orderCreateDto.getUserId()))
                .productOrders(new ArrayList<>(
                        basket.getProducts().stream()
                                .map(productIntoBasket -> {
                                            ProductOrder productOrder = ProductOrder.builder()
                                                    .finalPrice(productIntoBasket.getFinalPrice())
                                                    .price(productIntoBasket.getPrice())
                                                    .numberIntoBasket(productIntoBasket.getNumberIntoBasket())
                                                    .name(productIntoBasket.getName()).build();
                                            return productOrderRepository.save(productOrder);
                                        }
                                ).toList()))
                .date(LocalDate.now())
                .priceResult(basket.getPriceResult())
                .address(orderCreateDto.getAddress())
                .status(Status.IN_PROCESSING)
                .build();
    }


    @Transactional
    public void delete(Long orderId) {
        Optional<Order> currentOrder = orderRepository.findById(orderId);
        if (currentOrder.isPresent()) {
            orderRepository.deleteById(orderId);
            evictOrderCache(currentOrder.get().getUser().getId());
        } else {
            throw new ResourceNotFoundException("Order doesn't exists");
        }
    }

    @CacheEvict(value = "OrderService::getOrdersByUserId", key = "#userId")
    public void evictOrderCache(Long userId) {
        //this method is needed for caching after deletion
    }
}
