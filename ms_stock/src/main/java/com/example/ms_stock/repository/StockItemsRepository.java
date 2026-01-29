package com.example.ms_stock.repository;

import com.example.ms_stock.model.StockItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockItemsRepository extends JpaRepository<StockItems,Long> {
    boolean existsByStockIdAndIdProduit(Long stockId, Long idProduit);
    Optional<StockItems> findByStockIdAndIdProduit(Long stockId, Long idProduit);
   Optional<List<StockItems>> findByStockId(Long stockId);
    boolean existsByIdProduit(Long idProduit);

}
