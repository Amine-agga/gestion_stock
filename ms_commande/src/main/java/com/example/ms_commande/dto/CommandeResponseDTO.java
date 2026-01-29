package com.example.ms_commande.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommandeResponseDTO {
    private Long id;
    private String reference;
    private String realisateur;
    private Long idfournisseur;
    private String fournisseurNom;
    private String fournisseurEmail;
    private Long idStock;
    private String stockNom;
    private double montantTotal;
    private String commandeStatus;
    private Date dateCreation;
    private Date dateReception;
    private String motifAnnulation;
    private List<LigneCommandeResponseDTO> lignes;
}
