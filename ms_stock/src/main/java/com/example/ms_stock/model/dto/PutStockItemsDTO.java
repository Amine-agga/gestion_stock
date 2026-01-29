package com.example.ms_stock.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutStockItemsDTO {
    @PositiveOrZero(message = "La quantité ne peut pas être négative")
    private int quantite;
    @JsonProperty("seuil_minimum")
    @Column(name = "seuil_minimum") // Garde le nom SQL
    private int seuilMinimum;
}
