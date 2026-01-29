package com.example.ms_commande.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommandeUpdateRequestDTO {
    @NotNull
    private Long idfournisseur;
    @NotNull
    private Long idStock;
    @NotEmpty
    private List<LigneCommandeRequestDTO> lignes;
}
