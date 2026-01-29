package com.example.ms_stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_produit")
    private Long id_Produit;
    @Column(name = "id_stock")
    private Long id_Stock;
    private int quantite;
    private TypeMouvement typeMouvement;
    private LocalDateTime date;
    private String utilisateur;
}
