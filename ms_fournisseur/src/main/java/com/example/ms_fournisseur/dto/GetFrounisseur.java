package com.example.ms_fournisseur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetFrounisseur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
}
