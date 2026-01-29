package com.example.ms_fournisseur.mapper;

import com.example.ms_fournisseur.dto.GetFrounisseur;
import com.example.ms_fournisseur.dto.GetPublicFournisseur;
import com.example.ms_fournisseur.dto.PostFournisseur;
import com.example.ms_fournisseur.dto.PutFournisseur;
import com.example.ms_fournisseur.model.Fournisseur;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public Fournisseur PostToEntity(PostFournisseur postFournisseur){
        return new Fournisseur(
                null,
                postFournisseur.getNom(),
                postFournisseur.getPrenom(),
                postFournisseur.getEmail(),
                postFournisseur.getTelephone()
        );
    }
    public static GetFrounisseur EntityToDto(Fournisseur fournisseur){
        return new GetFrounisseur(
                fournisseur.getId(),
                fournisseur.getNom(),
                fournisseur.getPrenom(),
                fournisseur.getEmail(),
                fournisseur.getTelephone()
        );
    }
    public static GetPublicFournisseur entityToPublic(Fournisseur fournisseur){
        return new GetPublicFournisseur(
                fournisseur.getId(),
                fournisseur.getNom(),
                fournisseur.getPrenom(),
                fournisseur.getEmail(),
                fournisseur.getTelephone()
        );
    }
}
