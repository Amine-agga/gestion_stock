package com.example.ms_stock.repository;

import com.example.ms_stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    boolean existsByNom(String nom);
}
