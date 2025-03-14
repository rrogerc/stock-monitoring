package com.rrogerc.stock_monitor.producer;

import com.rrogerc.stock_monitor.model.StockPrice;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockPriceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public StockPriceProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPriceUpdate(String topic, StockPrice stockPrice) {
        kafkaTemplate.send(topic, stockPrice.toString());
    }
}
