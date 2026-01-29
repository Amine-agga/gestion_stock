package com.example.ms_stock.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProduitBean {
    private Long id;
    private String nom;
    private String urlImage;
    private String description;
    private Double prix;
    private int quantite;
    private boolean associer;
}
