package com.example.ms_stock.repository;

import com.example.ms_stock.model.MouvementStock;
import com.example.ms_stock.model.TypeMouvement;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MouvementRepository extends JpaRepository<MouvementStock,Long> {
    List<MouvementStock> findByTypeMouvement(TypeMouvement typeMouvement);
    boolean existsByTypeMouvement(TypeMouvement typeMouvement);
    @Query("SELECT m FROM MouvementStock m WHERE m.id_Stock = :stockId")
    List<MouvementStock> findByStockId(@Param("stockId") Long stockId);
}
