package com.example.ms_stock.batch.processor;

import com.example.ms_stock.batch.model.StockAlerte;
import com.example.ms_stock.model.StockItems;
import com.example.ms_stock.model.StockStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StockCritiqueProcessor implements ItemProcessor<StockItems, StockAlerte> {
    @Override
    public StockAlerte process(StockItems item) throws Exception {
        if(item.getStockStatus()== StockStatus.CRITIQUE || item.getStockStatus() == StockStatus.EN_RUPTURE){
            return new StockAlerte(
                    item.getStock().getId(),
                    item.getStock().getNom(),
                    item.getIdProduit(),
                    item.getQuantite(),
                    item.getSeuilMinimum(),
                    item.getStockStatus()
            );
        }
        return null;
    }
}
