package com.example.ms_produit.mapper;

import com.example.ms_produit.dto.GetDTO;
import com.example.ms_produit.dto.GetDTOPublic;
import com.example.ms_produit.dto.PostDTO;
import com.example.ms_produit.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class Mapper {
    public Produit getToEntity(PostDTO postDTO){
        return new Produit(
                null,
                postDTO.getNom(),
                postDTO.getUrlImage(),
                postDTO.getDescription(),
                postDTO.getPrix(),
                postDTO.getFournisseurIds()
        );
    }
    public static GetDTO toDTO(Produit produit){
        return new GetDTO(
                produit.getId(),
                produit.getNom(),
                produit.getUrlImage(),
                produit.getDescription(),
                produit.getPrix(),
                produit.getFournisseurIds()
        );
    }
    public static GetDTOPublic getDTOPublic(Produit produit){
        return new GetDTOPublic(
                produit.getId(),
                produit.getNom(),
                produit.getUrlImage(),
                produit.getDescription(),
                produit.getPrix(),
                produit.getFournisseurIds()
        );
    }

}
