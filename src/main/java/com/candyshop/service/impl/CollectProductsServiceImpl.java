package com.candyshop.service.impl;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Order;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.entity.ProductOrder;
import com.candyshop.entity.enums.Status;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.ProductOrderRepository;
import com.candyshop.service.BasketService;
import com.candyshop.service.CollectProductsService;
import com.candyshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CollectProductsServiceImpl implements CollectProductsService {

    private final UserService userService;
    private final BasketService basketService;
    private final ProductOrderRepository productOrderRepository;

    public Order collectProducts(OrderCreateDto orderCreateDto) {
        Basket basket = basketService.getBasketByUserId(orderCreateDto.getUserId());
        if (basket.getProducts().isEmpty()) {
            throw new ResourceNotFoundException("There are not products in the basket");
        }
        Order order = buildOrder(orderCreateDto, basket);
        basketService.deleteAllProductFromBasket(basket.getId());
        return order;
    }

    private Order buildOrder(final OrderCreateDto orderCreateDto, final Basket basket) {
        return Order.builder()
                .user(userService.getById(orderCreateDto.getUserId()))
                .productOrders(new ArrayList<>(
                        basket.getProducts().stream()
                                .map(this::getProductOrder).toList()))
                .date(LocalDate.now())
                .priceResult(basket.getPriceResult())
                .address(orderCreateDto.getAddress())
                .status(Status.IN_PROCESSING)
                .build();
    }

    @Transactional
    public ProductOrder getProductOrder(final ProductIntoBasket p) {
        ProductOrder productOrder = ProductOrder.builder()
                .finalPrice(p.getFinalPrice())
                .price(p.getPrice())
                .numberIntoBasket(p.getNumberIntoBasket())
                .name(p.getName()).build();
        return productOrderRepository.save(productOrder);
    }
}
