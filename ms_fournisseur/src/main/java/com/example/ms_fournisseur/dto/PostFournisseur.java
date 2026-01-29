package com.example.ms_fournisseur.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PostFournisseur {
    @NotNull(message = "The lastname field is required.")
    @Size(max = 30)
    private String nom;
    @NotNull(message = "The firstname field is required.")
    @Size(max =  30)
    private String prenom;
    @Email(message = "Please enter an email address.")
    private String email;
    @NotNull(message = "The number field is required.")
    @Size(max =  30)
    private String telephone;

}
