package com.example.ms_user.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUser{
    @NotBlank(message = "LastName is required")
    private String nom;
    @NotBlank(message = "FirstName is required")
    private String prenom;
    @NotBlank(message = "Email is required")
    private String email;
}
