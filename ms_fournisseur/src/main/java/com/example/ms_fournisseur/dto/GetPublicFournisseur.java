package com.example.ms_fournisseur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetPublicFournisseur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
}
