package com.example.ms_commande.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommandeRequestDTO {
    @NotNull
    private String realisateur;
    @NotNull
    private Long idfournisseur;
    @NotNull
    private Long idStock;
    @NotEmpty
    private List<LigneCommandeRequestDTO> lignes;
}
