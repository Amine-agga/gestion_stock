package com.example.ms_produit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDTO {
    private Long id;
    private String nom;
    private String urlImage;
    private String description;
    private Double prix;
    private List<Long> fournisseurIds;
}
