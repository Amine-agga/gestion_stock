package com.example.ms_user.model.dto;

import com.example.ms_user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    public LoginResponse(String token, String nom, String prenom, String email, Role role) {
        this.token = token;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }
}
