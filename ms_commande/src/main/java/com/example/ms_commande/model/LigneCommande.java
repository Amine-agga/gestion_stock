package com.example.ms_commande.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "commande")  // ✅ AJOUTER
@EqualsAndHashCode(exclude = "commande")
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idProduit;
    private String produitNom;
    private int quantite;
    private Double prix;
    private Double sousTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    @JsonBackReference
    private Commande commande;
}
