package com.candyshop.service;

import com.candyshop.dto.OrderCreateDto;
import com.candyshop.entity.Basket;
import com.candyshop.entity.Order;
import com.candyshop.entity.ProductIntoBasket;
import com.candyshop.entity.ProductOrder;
import com.candyshop.entity.enums.Status;
import com.candyshop.exception.ResourceNotFoundException;
import com.candyshop.repository.OrderRepository;
import com.candyshop.repository.ProductOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class OrderServiceTest {
    private final Long ID = 1L;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BasketService basketService;
    @Mock
    private UserService userService;
    @Mock
    private ProductOrderRepository prOrRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrdersByUserId() {
        List<Order> mockOrders = Arrays.asList(
                new Order(), new Order(), new Order()
        );
        when(orderRepository.findOrdersByUserId(ID)).thenReturn(mockOrders);

        List<Order> orders = orderService.getOrdersByUserId(ID);

        assertEquals(mockOrders, orders);
        assertEquals(3, orders.size());
    }

    @Test
    void testGetOrdersByUserId_NoOrdersFound() {
        when(orderRepository.findOrdersByUserId(ID))
                .thenReturn(Collections.emptyList());

        List<Order> orders = orderService.getOrdersByUserId(ID);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testCreateOrder_Success() {
        OrderCreateDto orderCreateDto = getOrderCreateDto();

        ProductIntoBasket productIntoBasket = getProductIntoBasket();
        ProductOrder productOrder = getProductOrder(productIntoBasket);

        Basket mockBasket = getBasket(productIntoBasket);

        when(prOrRepository.save(productOrder)).thenReturn(productOrder);
        when(basketService.getBasketByUserId(ID)).thenReturn(mockBasket);

        Order mockOrder = getOrder(mockBasket);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order createdOrder = orderService.create(orderCreateDto);

        assertNotNull(createdOrder);
        assertEquals(Status.IN_PROCESSING, createdOrder.getStatus());
        assertEquals(LocalDate.now(), createdOrder.getDate());
        assertEquals(mockBasket.getPriceResult(),
                createdOrder.getPriceResult());
    }

    @Test
    void testCreateOrder_EmptyBasket() {
        OrderCreateDto orderCreateDto = getOrderCreateDto();
        Basket mockBasket = new Basket();
        when(basketService.getBasketByUserId(ID)).thenReturn(mockBasket);

        assertThrows(NullPointerException.class,
                () -> orderService.create(orderCreateDto));
    }

    @Test
    void testCreateOrder_UserNotFound() {
        OrderCreateDto orderCreateDto = getOrderCreateDto();

        when(basketService.getBasketByUserId(ID)).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> orderService.create(orderCreateDto));
    }

    @Test
    void testDeleteOrder_Ok() {
        Order order = getOrder(getBasket(getProductIntoBasket()));

        when(orderRepository.findById(ID)).thenReturn(Optional.of(order));

        orderService.delete(ID);

        verify(orderRepository).findById(ID);
        verify(orderRepository).deleteById(ID);
    }

    @Test
    void testDeleteNonExistentOrder() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.delete(ID));

        verify(orderRepository).findById(ID);
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetOrdersSortedByDate() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order());
        orderList.add(new Order());

        when(orderRepository.findAllByOrderByDateAsc()).thenReturn(orderList);

        List<Order> retrievedOrders = orderService.getOrdersSortedByDate();

        assertNotNull(retrievedOrders);
        assertEquals(orderList.size(), retrievedOrders.size());
    }

    @Test
    void testGetOrdersSortedByDateWithEmptyList() {
        List<Order> orderList = new ArrayList<>();

        when(orderRepository.findAllByOrderByDateAsc()).thenReturn(orderList);

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrdersSortedByDate());

        verify(orderRepository).findAllByOrderByDateAsc();
    }

    private OrderCreateDto getOrderCreateDto() {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setUserId(ID);
        return orderCreateDto;
    }

    private Order getOrder(Basket mockBasket) {
        return Order.builder()
                .id(ID)
                .status(Status.IN_PROCESSING)
                .date(LocalDate.now())
                .priceResult(mockBasket.getPriceResult())
                .build();
    }

    private ProductIntoBasket getProductIntoBasket() {
        return ProductIntoBasket.builder()
                .id(ID)
                .finalPrice(BigDecimal.valueOf(100.0))
                .price(BigDecimal.valueOf(20.0))
                .numberIntoBasket(5)
                .build();
    }

    private Basket getBasket(ProductIntoBasket productIntoBasket) {
        return Basket.builder()
                .id(ID)
                .priceResult(BigDecimal.valueOf(100.0))
                .products(List.of(productIntoBasket))
                .build();
    }

    private ProductOrder getProductOrder(ProductIntoBasket p) {
        return ProductOrder.builder()
                .finalPrice(p.getFinalPrice())
                .price(p.getPrice())
                .numberIntoBasket(p.getNumberIntoBasket())
                .name(p.getName())
                .build();
    }
}