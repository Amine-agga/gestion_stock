package com.example.ms_fournisseur.controller;

import com.example.ms_fournisseur.dto.GetFrounisseur;
import com.example.ms_fournisseur.dto.GetPublicFournisseur;
import com.example.ms_fournisseur.dto.PostFournisseur;
import com.example.ms_fournisseur.dto.PutFournisseur;
import com.example.ms_fournisseur.model.Fournisseur;
import com.example.ms_fournisseur.service.FournisseurService;
import com.netflix.discovery.converters.Auto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseur")
public class FournisseurController {
    @Autowired
    private FournisseurService service;
    @PostMapping
    public ResponseEntity<GetFrounisseur> createFournisseur(@Valid @RequestBody PostFournisseur postFournisseur){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFournisseur(postFournisseur));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetFrounisseur> getFrounisseur(@PathVariable Long id){
        return ResponseEntity.ok(service.getFrounisseur(id));
    }
    @GetMapping
    public ResponseEntity<List<GetFrounisseur>> getFrounisseurs(){
        return ResponseEntity.ok(service.getFrounisseurs());
    }
    @GetMapping("/public/{id}")
    public ResponseEntity<GetPublicFournisseur> getPublicFournisseur(@PathVariable Long id){
        return ResponseEntity.ok(service.getPublicFournisseur(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GetFrounisseur> updateFournisseur(@PathVariable Long id , @Valid @RequestBody PutFournisseur putFournisseur){
        return ResponseEntity.ok(service.updateFounisseur(id,putFournisseur));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id){
        service.deleteFourniseur(id);
        return ResponseEntity.noContent().build();
    }
}
