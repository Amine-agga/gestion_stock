package com.example.ms_commande.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "ligneCommandes")
@EqualsAndHashCode(exclude = "ligneCommandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String realisateur;
    private Long idfournisseur;
    private String fournisseurNom;
    private String fournisseurEmail;
    private Long idStock;
    private String stockNom;
    private double montantTotal;
    @Enumerated(EnumType.STRING)
    private CommandeStatus commandeStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReception;
    private String motifAnnulation;
    @Column(unique = true, length = 100)
    private String validationToken;
    @Column
    private Date tokenExpiration;
    @Column
    private Date dateValidation;
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LigneCommande> ligneCommandes = new ArrayList<>();
    public boolean peutEtreModifiee() {
        return commandeStatus == CommandeStatus.EN_ATTENTE;
    }
    public boolean peutEtreSupprimee(){
        return commandeStatus == CommandeStatus.EN_ATTENTE ||
                commandeStatus == CommandeStatus.ANNULEE;
    }
    public void addLigne(LigneCommande ligne) {
        ligneCommandes.add(ligne);
        ligne.setCommande(this);
    }
    public void removeLigne(LigneCommande ligne) {
        ligneCommandes.remove(ligne);
        ligne.setCommande(null);
    }
}
