package com.example.ms_produit.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO {
    @NotNull
    @Size(min = 1 , max = 100)
    private String nom;
    @Size(min = 1 , max = 400)
    private String description;
    @Positive
    private Double prix;
    @NotBlank
    @Size(min = 1,max = 200)
    private String urlImage;
    private List<Long> fournisseurIds;
}
