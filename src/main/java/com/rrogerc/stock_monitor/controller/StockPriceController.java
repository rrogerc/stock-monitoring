package com.rrogerc.stock_monitor.controller;

import com.rrogerc.stock_monitor.model.StockPrice;
import com.rrogerc.stock_monitor.producer.StockPriceProducer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockPriceController {

    private final StockPriceProducer producer;
    private final LinkedList<StockPrice> priceHistory = new LinkedList<>();

    public StockPriceController(StockPriceProducer producer) {
        this.producer = producer;
    }

    // POST: Trigger stock price update
    @PostMapping("/send")
    public String sendStockPrice(@RequestParam String symbol,
                                 @RequestParam BigDecimal price) {
        StockPrice stockPrice = new StockPrice(symbol, price, LocalDateTime.now());
        producer.sendPriceUpdate("stock-prices", stockPrice);

        // Store in history (keep last 10 updates)
        if (priceHistory.size() >= 10) {
            priceHistory.removeFirst();
        }
        priceHistory.add(stockPrice);

        return "Stock price sent: " + stockPrice;
    }

    // GET: Retrieve last N stock price updates
    @GetMapping("/history")
    public List<StockPrice> getPriceHistory(@RequestParam(defaultValue = "5") int limit) {
        int startIndex = Math.max(0, priceHistory.size() - limit);
        return priceHistory.subList(startIndex, priceHistory.size());
    }
}
