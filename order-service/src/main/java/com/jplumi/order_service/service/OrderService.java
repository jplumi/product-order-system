package com.jplumi.order_service.service;

import com.jplumi.order_service.dto.InventoryResponse;
import com.jplumi.order_service.dto.OrderItemDto;
import com.jplumi.order_service.dto.OrderRequest;
import com.jplumi.order_service.model.Order;
import com.jplumi.order_service.model.OrderItem;
import com.jplumi.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItems()
                .stream().map(this::dtoToOrderItem).toList();
        newOrder.setOrderItems(orderItems);

        // Check stock
        List<String> itemsSkuCodes = orderItems
                .stream().map(OrderItem::getSku).toList();

        InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
                .uri("http://inventory-service/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("skuCodes", itemsSkuCodes)
                                .build())
                .retrieve().
                bodyToMono(InventoryResponse[].class).
                block();

        boolean allItemsInStock = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);

        if(allItemsInStock) {
            orderRepository.save(newOrder);
        } else {
            throw new IllegalArgumentException("Some products are not in stock at the moment.");
        }
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
