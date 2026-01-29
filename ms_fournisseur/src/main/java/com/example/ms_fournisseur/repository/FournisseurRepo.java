package com.example.ms_fournisseur.repository;

import com.example.ms_fournisseur.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FournisseurRepo extends JpaRepository<Fournisseur,Long> {
    boolean existsByEmail(String email);
    boolean existsByTelephone(String Telephone);
    Optional<Fournisseur> findByEmail(String email);
    Optional<Fournisseur> findByTelephone(String telephone);
}
