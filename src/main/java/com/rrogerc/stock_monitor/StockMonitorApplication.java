package com.rrogerc.stock_monitor;

import com.rrogerc.stock_monitor.model.StockPrice;
import com.rrogerc.stock_monitor.producer.StockPriceProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class StockMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMonitorApplication.class, args);
    }

    @Bean
    CommandLineRunner sendStockPrices(StockPriceProducer producer) {
        return args -> {
            // Simulate stock price updates
            producer.sendPriceUpdate("stock-prices",
                    new StockPrice("AAPL", BigDecimal.valueOf(150), LocalDateTime.now()));

            producer.sendPriceUpdate("stock-prices",
                    new StockPrice("GOOGL", BigDecimal.valueOf(2800), LocalDateTime.now()));

            producer.sendPriceUpdate("stock-prices",
                    new StockPrice("MSFT", BigDecimal.valueOf(299), LocalDateTime.now()));
        };
    }
}
