package com.rrogerc.stock_monitor.consumer;

import com.rrogerc.stock_monitor.model.StockPrice;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class StockPriceConsumer {

    private final LinkedList<StockPrice> priceHistory = new LinkedList<>();

    @KafkaListener(topics = "stock-prices", groupId = "stock-group")
    public void consume(String message) {
        System.out.println("Received stock price update: " + message);

        // Deserialize manually (since it's stored as a string)
        String[] data = message.replace("StockPrice{", "")
                .replace("}", "")
                .split(", ");
        String symbol = data[0].split("=")[1];
        BigDecimal price = new BigDecimal(data[1].split("=")[1]);
        LocalDateTime timestamp = LocalDateTime.parse(data[2].split("=")[1]);

        StockPrice stockPrice = new StockPrice(symbol, price, timestamp);

        // Keep the last 10 updates
        if (priceHistory.size() >= 10) {
            priceHistory.removeFirst();
        }
        priceHistory.add(stockPrice);
    }

    public List<StockPrice> getPriceHistory(int limit) {
        int startIndex = Math.max(0, priceHistory.size() - limit);
        return priceHistory.subList(startIndex, priceHistory.size());
    }
}
