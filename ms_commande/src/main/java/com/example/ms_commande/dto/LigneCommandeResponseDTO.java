package com.example.ms_commande.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LigneCommandeResponseDTO {
    private Long idProduit;
    private String produitNom;
    private int quantite;
    private Double prix;
    private Double sousTotal;
}
