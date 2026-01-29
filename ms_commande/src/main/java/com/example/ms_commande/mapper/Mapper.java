package com.example.ms_commande.mapper;

import com.example.ms_commande.dto.CommandeResponseDTO;
import com.example.ms_commande.dto.LigneCommandeResponseDTO;
import com.example.ms_commande.model.Commande;
import com.example.ms_commande.model.LigneCommande;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public static CommandeResponseDTO toResponseDTO(Commande commande){
       CommandeResponseDTO dto = new CommandeResponseDTO();
       dto.setId(commande.getId());
       dto.setRealisateur(commande.getRealisateur());
       dto.setReference(commande.getReference());
       dto.setIdfournisseur(commande.getIdfournisseur());
       dto.setFournisseurNom(commande.getFournisseurNom());
       dto.setFournisseurEmail(commande.getFournisseurEmail());
       dto.setIdStock(commande.getIdStock());
       dto.setStockNom(commande.getStockNom());
       dto.setMontantTotal(commande.getMontantTotal());
       dto.setMotifAnnulation(commande.getMotifAnnulation());
       dto.setCommandeStatus(commande.getCommandeStatus().toString());
       dto.setDateCreation(commande.getDateCreation());
       dto.setDateReception(commande.getDateReception());
       if(commande.getLigneCommandes() != null) {
           dto.setLignes(commande.getLigneCommandes().stream().map(Mapper::toLigneResponseDTO).toList());
       }
       return dto;
    }
    public static LigneCommandeResponseDTO toLigneResponseDTO(LigneCommande ligneCommande){
        return new LigneCommandeResponseDTO(
                ligneCommande.getIdProduit(),
                ligneCommande.getProduitNom(),
                ligneCommande.getQuantite(),
                ligneCommande.getPrix(),
                ligneCommande.getSousTotal()
        );
    }
}
