package com.example.ms_commande.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeUpdateDTO {
    private Long idfournisseur;
    private Long idStock;
    private List<LigneCommandeRequestDTO> lignes;
}
