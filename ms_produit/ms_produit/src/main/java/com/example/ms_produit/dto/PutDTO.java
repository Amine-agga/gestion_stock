package com.example.ms_produit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutDTO {
    @NotEmpty
    @Size(min = 1,max = 200)
    private String urlImage;
    @Positive
    private Double prix;
    private List<Long> fournisseurIds;
}
