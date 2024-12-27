package com.jplumi.order_service.service;

import com.jplumi.order_service.dto.OrderItemDto;
import com.jplumi.order_service.dto.OrderRequest;
import com.jplumi.order_service.model.Order;
import com.jplumi.order_service.model.OrderItem;
import com.jplumi.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItems()
                .stream().map(this::dtoToOrderItem).toList();
        newOrder.setOrderItems(orderItems);

        orderRepository.save(newOrder);
    }

    private OrderItem dtoToOrderItem(OrderItemDto dto) {
        return new OrderItem(
                null,
                dto.getSku(),
                dto.getPrice(),
                dto.getQuantity()
        );
    }

}
