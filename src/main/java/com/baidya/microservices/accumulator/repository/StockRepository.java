package com.baidya.microservices.accumulator.repository;

import com.baidya.microservices.accumulator.pojo.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
