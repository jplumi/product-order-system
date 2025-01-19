package com.jplumi.notification_service;

import com.jplumi.notification_service.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleOrderNotification(OrderPlacedEvent orderPlacedEvent) {
        // TODO: Send notification
        log.info("Notification Received: order {}", orderPlacedEvent.getOrderNumber());
    }
}
