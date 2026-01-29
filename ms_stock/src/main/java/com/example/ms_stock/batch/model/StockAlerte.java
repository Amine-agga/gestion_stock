package com.example.ms_stock.batch.model;

import com.example.ms_stock.model.StockStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class StockAlerte {
    private Long stockId;
    private String stockNom;
    private Long idProduit;           // ✅ Cohérent avec StockItems
    private int quantite;             // ✅ Cohérent avec StockItems
    private int seuilMinimum;         // ✅ Cohérent avec StockItems
    private StockStatus stockStatus;  // ✅ Utilise l'enum StockStatus
    private String message;
    private LocalDateTime dateAlerte;
    public StockAlerte(){
        this.dateAlerte = LocalDateTime.now();
    }
    public StockAlerte(Long stockId, String stockNom, Long idProduit, int quantite, int seuilMinimum,StockStatus status){
        this.stockId=stockId;
        this.stockNom=stockNom;
        this.idProduit= idProduit;
        this.quantite=quantite;
        this.seuilMinimum=seuilMinimum;
        this.stockStatus=status;
        this.message=genererMessage();
        this.dateAlerte= LocalDateTime.now();
    }
    private String genererMessage() {
        switch (stockStatus){
            case EN_RUPTURE :
                return String.format("ALERTE CRITIQUE : Rupture de stock pour le produit %d dans le stock '%s'",idProduit,stockNom);
            case CRITIQUE:
                return String.format("ALERTE : Stock faible pour le produit %d dans le stock '%s' (%d unites restantes , seuil %d ",idProduit,stockNom,quantite,seuilMinimum);
            case NORMAL:
            default:
                return "Stock Normal";
        }
    }
}
