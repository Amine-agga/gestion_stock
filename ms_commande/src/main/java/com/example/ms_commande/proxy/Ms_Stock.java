package com.example.ms_commande.proxy;

import com.example.ms_commande.bean.StockBean;
import com.example.ms_commande.bean.StockItemsBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stock-service")
public interface Ms_Stock {
    @GetMapping("/api/stocks/{id}")
    public StockBean getStock(@PathVariable Long id);
    @GetMapping("/api/stocks/stockItems/{id_stockItems}")
    public StockItemsBean getStockItems(@PathVariable Long id_stockItems);
    @PostMapping("/api/stocks/{id_stock}/produit/{id_produit}/crediter/{quantite}")
    public StockItemsBean crediterStock(@PathVariable Long id_stock, @PathVariable Long id_produit, @PathVariable int quantite, @RequestParam String user);
    @PostMapping("/api/stocks/{id_stock}/produit/{id_produit}/debiter/{quantite}")
    public StockItemsBean debiterStock(@PathVariable Long id_stock,@PathVariable Long id_produit,@PathVariable int quantite,@RequestParam String user);
}
