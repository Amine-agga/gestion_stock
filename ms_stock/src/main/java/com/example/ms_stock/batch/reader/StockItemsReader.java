package com.example.ms_stock.batch.reader;

import com.example.ms_stock.batch.model.StockAlerte;
import com.example.ms_stock.model.StockItems;
import com.example.ms_stock.repository.StockItemsRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
@Component
public class StockItemsReader implements ItemReader<StockItems> {
    @Autowired
    private StockItemsRepository stockItemsRepository;
    private Iterator<StockItems> stockItemsIterator;
    @Override
    public StockItems read() throws Exception {
        if(stockItemsIterator==null){
            List<StockItems> stockItemsList = stockItemsRepository.findAll();
            System.out.println("📦 Batch Reader: " + stockItemsList.size() + " items de stock chargés pour analyse");
            stockItemsIterator = stockItemsList.iterator();
        }
        if(stockItemsIterator.hasNext()){
            return stockItemsIterator.next();
        }else{
            stockItemsIterator = null;
            return null;
        }
    }
}
