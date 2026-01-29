package com.example.ms_produit.repository;

import com.example.ms_produit.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProduitRepo extends JpaRepository<Produit,Long> {
    boolean existsByNom(String nom);
    Optional<Produit> findByNom(String nom);
}
