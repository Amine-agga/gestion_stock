package com.example.ms_stock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "id_produit") // Garde le nom SQL
    private Long idProduit;      // camelCase en Java
    @PositiveOrZero(message = "La quantité ne peut pas être négative")
    private int quantite;
    @PositiveOrZero(message = "Le seuil ne peut pas être négative")
    @Column(name = "seuil_minimum") // Garde le nom SQL
    private int seuilMinimum;       // camelCase en Java
    private LocalDateTime modification;
    private String description;
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
}
