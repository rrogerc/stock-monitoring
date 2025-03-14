package com.rrogerc.stock_monitor.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockPriceConsumer {

    @KafkaListener(topics = "stock-prices", groupId = "stock-group")
    public void consume(String message) {
        System.out.println("Received stock price update: " + message);
    }
}
