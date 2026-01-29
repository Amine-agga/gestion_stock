package com.example.ms_stock.service;

import com.example.ms_stock.bean.ProduitBean;
import com.example.ms_stock.exception.*;
import com.example.ms_stock.model.*;
import com.example.ms_stock.model.dto.PutStockItemsDTO;
import com.example.ms_stock.proxy.MS_produit;
import com.example.ms_stock.repository.MouvementRepository;
import com.example.ms_stock.repository.StockItemsRepository;
import com.example.ms_stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockItemsRepository stockItemsRepository;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private MS_produit msProduit;
    private static final String DEFAULT_USER = "SYSTEM";
    /*-----------------------------STOCK-----------------------------*/
    /*---------------------------STOCK_CRUD--------------------------*/
    public Stock createStock(String nom){
        Stock stock = new Stock();
        stock.setNom(nom);
        if(stockRepository.existsByNom(nom)) throw new StockAlReadyExistException("Stock All Ready Exist.");
        return stockRepository.save(stock);
    }
    public Stock getStock(Long id){
        return stockRepository.findById(id).orElseThrow(()-> new StockNotFoundException("Stock Not Found."));
    }
    public List<Stock> getStocks(){
        return stockRepository.findAll();
    }
    public void deleteStock(Long id){
        if(!stockRepository.existsById(id)) throw new StockNotFoundException("Stock Not Found.");
        stockRepository.deleteById(id);
    }
    public Stock updateStock(Long id,Stock stock){
        Stock stock1 = getStock(id);
        if(stockRepository.existsByNom(stock.getNom())) throw new StockAlReadyExistException("Stock All Ready Exist.");
        stock1.setNom(stock.getNom());
        return stockRepository.save(stock1);
    }
    /*-----------------------------STOCK_ITEMS-----------------------------*/
    /*---------------------------STOCK_ITEMS_CRUD--------------------------*/
    public StockItems createItems(Long id_stock, Long id_produit, int quantite, int seuil_minimum) {

        ProduitBean produitBean = msProduit.getDTOPublic(id_produit);
        if(produitBean == null) throw new ProduitNotFoundException("Product Not Found .");
        Stock stock = getStock(id_stock);

        if (stockItemsRepository.existsByStockIdAndIdProduit(stock.getId(),produitBean.getId())) {
            throw new ProduitAlReadyExistException("Product Already Exists");
        }
        else{
            StockItems stockItems = new StockItems();
            stockItems.setIdProduit(id_produit);
            stockItems.setQuantite(quantite);
            stockItems.setSeuilMinimum(seuil_minimum);
            updateStockStatus(stockItems);
            stockItems.setStock(stock);
            stockItems.setModification(LocalDateTime.now());
            mouvementRepository.save(createMouvement(id_stock,id_produit,quantite,DEFAULT_USER));
            return stockItemsRepository.save(stockItems);
        }
    }
    /*-----------ALL_ITEMS_STOCK----------*/
    // Les produits par stock
    public List<StockItems> getStockItemsById(Long id_stock){
        return stockItemsRepository.findByStockId(id_stock).orElseThrow(()-> new StockNotFoundException("Stock Not Found."));
    }
    public StockItems getStockItemsByStockIdAndIdProduit(Long id_stock,Long id_produit){
        return stockItemsRepository.findByStockIdAndIdProduit(id_stock, id_produit).orElseThrow(()-> new StockItemsNotFoundByIdProduitByStockIdException(""));
    }
    public List<StockItems> getAllStockItems(){
        return stockItemsRepository.findAll();
    }
    public StockItems getStockItem(Long id){
        return stockItemsRepository.findById(id).orElseThrow(()-> new StockItemsNotFoundException("StockItems Not Found."));
    }
    public StockItems updateStockItems(Long id,PutStockItemsDTO putStockItemsDTO){
        StockItems stockItems1 = getStockItem(id);
        stockItems1.setQuantite(putStockItemsDTO.getQuantite());
        stockItems1.setSeuilMinimum(putStockItemsDTO.getSeuilMinimum());
        stockItems1.setModification(LocalDateTime.now());
        updateStockStatus(stockItems1);
        mouvementRepository.save(mofidierMouvement(stockItems1.getStock().getId(),stockItems1.getIdProduit(),stockItems1.getQuantite(),DEFAULT_USER));
        return stockItemsRepository.save(stockItems1);
    }
    public void deletStockItems(Long id){
        StockItems stockItems = getStockItem(id);
        mouvementRepository.save(deleteMouvement(stockItems.getStock().getId(),stockItems.getIdProduit(),stockItems.getQuantite(),DEFAULT_USER));
        stockItemsRepository.deleteById(id);
    }
    private void updateStockStatus(StockItems stockItems) {
        if (stockItems.getQuantite() <= 0) {
            stockItems.setStockStatus(StockStatus.EN_RUPTURE);
            stockItems.setDescription("Stock épuisé");
        } else if (stockItems.getQuantite() <= stockItems.getSeuilMinimum()) {
            stockItems.setStockStatus(StockStatus.CRITIQUE);
            stockItems.setDescription("Stock faible");
        } else {
            stockItems.setStockStatus(StockStatus.NORMAL);
            stockItems.setDescription("Stock suffisant");
        }
    }
    /*---------------------------STOCK_ITEMS_METIER--------------------------*/
    @Transactional
    public StockItems debiterStock(Long id_stock,Long id_produit,int quantite,String user) {
        StockItems stockItems = stockItemsRepository.findByStockIdAndIdProduit(id_stock,id_produit).orElseThrow(()-> new StockItemsNotFoundException("StockItems Not Found."));
        if(stockItems.getQuantite()<quantite) throw new QuantiteInssufiantException("Not enough quantity available.");
        else{
            stockItems.setQuantite(stockItems.getQuantite()-quantite);
            updateStockStatus(stockItems);
            if(user == null) mouvementRepository.save(sortieMouvement(id_stock,id_produit,quantite,DEFAULT_USER));
            else mouvementRepository.save(sortieMouvement(id_stock,id_produit,quantite,user));
            return stockItemsRepository.save(stockItems);
        }
    }
    @Transactional
    public StockItems crediterStock(Long id_stock,Long id_produit,int quantite,String user){
        StockItems stockItems = stockItemsRepository.findByStockIdAndIdProduit(id_stock,id_produit).orElseThrow(()-> new StockItemsNotFoundException("StockItems Not Found."));
        stockItems.setQuantite(stockItems.getQuantite()+quantite);
        updateStockStatus(stockItems);
        if(user == null) mouvementRepository.save(entreMouvement(id_stock,id_produit,quantite,DEFAULT_USER));
        else mouvementRepository.save(entreMouvement(id_stock, id_produit, quantite, user));
        return stockItemsRepository.save(stockItems);
    }
    /*------------------------MOUVEMENT_STOCK_ACTION-----------------------*/
    public MouvementStock createMouvement(Long id_stock, Long id_produit, int quantite,String utilisateur){
        return new MouvementStock(
                null,
                id_produit,
                id_stock,
                quantite,
                TypeMouvement.CREATION,
                LocalDateTime.now(),
                utilisateur
        );
    }
    public MouvementStock sortieMouvement(Long id_stock, Long id_produit, int quantite,String utilisateur){
        return new MouvementStock(
                null,
                id_produit,
                id_stock,
                quantite,
                TypeMouvement.SORTIE,
                LocalDateTime.now(),
                utilisateur
        );
    }
    public MouvementStock entreMouvement(Long id_stock, Long id_produit, int quantite,String utilisateur){
        return new MouvementStock(
                null,
                id_produit,
                id_stock,
                quantite,
                TypeMouvement.ENTREE,
                LocalDateTime.now(),
                utilisateur
        );
    }
    public MouvementStock mofidierMouvement(Long id_stock, Long id_produit, int quantite,String utilisateur){
        return new MouvementStock(
                null,
                id_produit,
                id_stock,
                quantite,
                TypeMouvement.MODIFICATION,
                LocalDateTime.now(),
                utilisateur
        );
    }
    public MouvementStock deleteMouvement(Long id_stock, Long id_produit, int quantite,String utilisateur){
        return new MouvementStock(
                null,
                id_produit,
                id_stock,
                quantite,
                TypeMouvement.SUPPRESSION,
                LocalDateTime.now(),
                utilisateur
        );
    }
    /*-----------------------MOUVEMENT-----------------------*/
    public List<MouvementStock> getMouvementByType(TypeMouvement typeMouvement){
        if(!mouvementRepository.existsByTypeMouvement(typeMouvement)) throw new TypeMouvementDidntExistsException("This Type Didn't Exists.");
        return mouvementRepository.findByTypeMouvement(typeMouvement);
    }

    public List<MouvementStock> getMouvement(){
        return mouvementRepository.findAll();
    }
    public List<MouvementStock> getMouvementByStock(Long id){
        if(!stockRepository.existsById(id)) throw new StockNotFoundException("Stock Not Found.");
        return mouvementRepository.findByStockId(id);
    }
}