package com.example.ms_produit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String urlImage;
    private String description;
    private Double prix;
    @ElementCollection
    @CollectionTable(
            name = "produit_fournisseurs",
            joinColumns = @JoinColumn(name = "produit_id")
    )
    @Column(name = "fournisseur_id")
    private List<Long> fournisseurIds;
}
