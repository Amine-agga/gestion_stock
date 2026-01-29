package com.example.ms_commande.repository;

import com.example.ms_commande.model.Commande;
import com.example.ms_commande.model.CommandeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande,Long> {
    List<Commande> findByIdfournisseur(Long idfournisseur);
    List<Commande> findByCommandeStatus(CommandeStatus commandeStatus);
    Optional<Commande> findByReference(String reference);
    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.ligneCommandes WHERE c.id = :id")
    Optional<Commande> findByIdWithLignes(@Param("id") Long id);
    @Query("SELECT DISTINCT c FROM Commande c LEFT JOIN FETCH c.ligneCommandes")
    List<Commande> findAllWithLignes();
    Optional<Commande> findTopByReferenceLikeOrderByReferenceDesc(String referencePattern);
    Optional<Commande> findByValidationToken(String validationToken);
}
