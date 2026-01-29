package com.example.ms_produit.controller;

import com.example.ms_produit.bean.FournisseurBean;
import com.example.ms_produit.dto.GetDTO;
import com.example.ms_produit.dto.GetDTOPublic;
import com.example.ms_produit.dto.PostDTO;
import com.example.ms_produit.dto.PutDTO;
import com.example.ms_produit.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ControllerProduit {
    @Autowired
    private ProduitService produitService;
    @GetMapping
    public ResponseEntity<List<GetDTO>> getProduits(){
        return ResponseEntity.ok(produitService.getAllProduit());
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetDTO> getProduit(@PathVariable Long id){
        return ResponseEntity.ok(produitService.getProduitById(id));
    }
    @GetMapping("/{id}/public")
    public ResponseEntity<GetDTOPublic> getDTOPublic(@PathVariable Long id){
        return ResponseEntity.ok(produitService.getDTOPublic(id));
    }
    @GetMapping("/ByNom/{nom}")
    public ResponseEntity<GetDTO> getProduitByNom(@PathVariable String nom){
        return ResponseEntity.ok(produitService.getProduitByName(nom));
    }
    @GetMapping("/fournisseur/{id}")
    public ResponseEntity<List<GetDTOPublic>> getProduitByfournisseur(@PathVariable Long id){
        return ResponseEntity.ok(produitService.getProduitByFournisseur(id));
    }
    @GetMapping("/{idProduit}/fournisseur/{idFournisseur}")
    public ResponseEntity<Boolean> isProduitDisponibleChezFournisseur(@PathVariable Long idProduit,@PathVariable Long idFournisseur){
        return ResponseEntity.ok(produitService.isProduitDisponibleChezFournisseur(idProduit, idFournisseur));
    }
    @PostMapping("/{idProduit}/fournisseur/{idFournisseur}")
    public ResponseEntity<Void> associerFournisseur(@PathVariable Long idProduit, @PathVariable Long idFournisseur){
        produitService.associerFournisseur(idProduit, idFournisseur);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{idProduit}/fournisseur/{idFournisseur}")
    public ResponseEntity<Void> dissocierFournisseur(@PathVariable Long idProduit, @PathVariable Long idFournisseur){
        produitService.dissocierFournisseur(idProduit, idFournisseur);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/fournisseurs")
    public ResponseEntity<List<FournisseurBean>> getFournisseursByProduit(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.getFournisseursByProduit(id));
    }
    @PostMapping
    public ResponseEntity<GetDTO> createProduit(@Valid @RequestBody PostDTO postDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.createProduit(postDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GetDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody PutDTO putDTO){
        return ResponseEntity.ok(produitService.updateProduit(id,putDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id){
        produitService.deleteproduit(id);
        return ResponseEntity.noContent().build();
    }
}
