package com.jplumi.order_service.service;

import com.jplumi.order_service.dto.InventoryResponse;
import com.jplumi.order_service.dto.OrderItemDto;
import com.jplumi.order_service.dto.OrderRequest;
import com.jplumi.order_service.event.OrderPlacedEvent;
import com.jplumi.order_service.model.Order;
import com.jplumi.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Autowired
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should place order successfully")
    void placeOrder_Success() {
        // Arrange
        InventoryResponse[] inventoryResponses = { new InventoryResponse("1234", true) };
        mockWebClient(inventoryResponses);

        // Act
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemDto("1234", BigDecimal.valueOf(999.99), 3)
        ));
        orderService.placeOrder(orderRequest);

        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaTemplate, times(1)).send( eq("notificationTopic"), any(OrderPlacedEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when items not in stock")
    void placeOrder_Failure() {
        // Arrange
        InventoryResponse[] inventoryResponses = { new InventoryResponse("1234", false) };
        mockWebClient(inventoryResponses);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            OrderRequest orderRequest = new OrderRequest(List.of(
                    new OrderItemDto("1234", BigDecimal.valueOf(999.99), 3)
            ));
            orderService.placeOrder(orderRequest);
        });
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), any(OrderPlacedEvent.class));
    }

    private void mockWebClient(InventoryResponse[] inventoryResponses) {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));
    }
}