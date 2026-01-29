package com.example.ms_commande.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LigneCommandeRequestDTO {
    @NotNull
    private Long idProduit;
    @NotNull
    @Positive
    @Min(value = 1)
    private int quantite;
}
