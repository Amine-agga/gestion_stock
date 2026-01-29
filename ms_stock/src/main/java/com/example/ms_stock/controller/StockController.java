package com.example.ms_stock.controller;

import com.example.ms_stock.model.MouvementStock;
import com.example.ms_stock.model.Stock;
import com.example.ms_stock.model.StockItems;
import com.example.ms_stock.model.TypeMouvement;
import com.example.ms_stock.model.dto.PutStockItemsDTO;
import com.example.ms_stock.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    @Autowired
    private StockService service;
    /*------------Stock-----------*/
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks(){
        return ResponseEntity.ok(service.getStocks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id){
        return ResponseEntity.ok(service.getStock(id));
    }
    @PostMapping
    public ResponseEntity<Stock> createStock(@Valid @RequestParam String nom){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStock(nom));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id,@Valid @RequestBody Stock stock){
        return ResponseEntity.ok(service.updateStock(id,stock));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id){
        service.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
    /*------------StockItems-----------*/
    @GetMapping("/stockItems")
    public ResponseEntity<List<StockItems>> getAllStockItems(){
        return ResponseEntity.ok(service.getAllStockItems());
    }
    @GetMapping("/stockItems/{id_stockItems}")
    public ResponseEntity<StockItems> getStockItems(@PathVariable Long id_stockItems){
        return ResponseEntity.ok(service.getStockItem(id_stockItems));
    }
    @GetMapping("/stock/{id_stock}")
    public ResponseEntity<List<StockItems>> getStockItemsById(@PathVariable Long id_stock){
        return ResponseEntity.ok(service.getStockItemsById(id_stock));
    }
    @GetMapping("/{id_stock}/produit/{id_produit}")
    public ResponseEntity<StockItems> getStockItemsByStockIdAndIdProduit(@PathVariable Long id_stock,@PathVariable Long id_produit){
        return ResponseEntity.ok(service.getStockItemsByStockIdAndIdProduit(id_stock, id_produit));
    }
    @PostMapping("/createItems")
    public ResponseEntity<StockItems> createStockItems(@RequestParam Long id_stock, @RequestParam Long id_produit, @RequestParam int quantite, @RequestParam int seuil_minimum){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createItems(id_stock,id_produit,quantite,seuil_minimum));
    }
    @PutMapping("/stockItems/{id}")
    public ResponseEntity<StockItems> updateStockItems(@PathVariable Long id,@Valid @RequestBody PutStockItemsDTO stockItems){
        return ResponseEntity.ok(service.updateStockItems(id, stockItems));
    }
    @DeleteMapping("/stockItems/{id}")
    public ResponseEntity<Void> deleteStockItems(@PathVariable Long id){
        service.deletStockItems(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id_stock}/produit/{id_produit}/debiter/{quantite}")
    public StockItems debiterStock(@PathVariable Long id_stock,@PathVariable Long id_produit,@PathVariable int quantite,@RequestParam String user){
        return service.debiterStock(id_stock, id_produit, quantite,user);
    }
    @PostMapping("/{id_stock}/produit/{id_produit}/crediter/{quantite}")
    public StockItems crediterStock(@PathVariable Long id_stock,@PathVariable Long id_produit,@PathVariable int quantite,@RequestParam String user){
        return service.crediterStock(id_stock, id_produit, quantite,user);
    }
    @GetMapping("/mouvement/type")
    public List<MouvementStock> getMouvementByType(@RequestParam TypeMouvement typeMouvement){
        return service.getMouvementByType(typeMouvement);
    }
    @GetMapping("/mouvement")
    public List<MouvementStock> getMouvement(){
        return service.getMouvement();
    }
    @GetMapping("/mouvement/stock/{id}")
    public List<MouvementStock> getMouvementByStock(@PathVariable Long id){
        return service.getMouvementByStock(id);
    }
}
