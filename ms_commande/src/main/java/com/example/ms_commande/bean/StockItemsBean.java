package com.example.ms_commande.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockItemsBean {
    private Long id;
    private Long idProduit;
    private int quantite;
    private int seuilMinimum;
    private LocalDateTime modification;
    private String description;
    private String stockStatus;
}
