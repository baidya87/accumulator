package com.baidya.microservices.accumulator.service;

import com.baidya.microservices.accumulator.pojo.Stock;
import com.baidya.microservices.accumulator.repository.StockRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ConsumeService {

    private final StockRepository stockRepository;

    public ConsumeService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @KafkaListener(id = "kl2", groupId = "${spring.kafka.consumer.group.id}", topics = "stock.info")
    public void consume(@Payload String stock, Acknowledgment acknowledgment){
        System.out.println("Consumed from stock.info ["+stock+"]");
        Stock stock1 = new Stock();
        stock1.setStockName(stock);
        stockRepository.save(stock1);
        acknowledgment.acknowledge();
    }
}
