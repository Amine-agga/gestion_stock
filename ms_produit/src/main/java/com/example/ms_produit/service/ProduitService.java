package com.example.ms_produit.service;

import com.example.ms_produit.bean.FournisseurBean;
import com.example.ms_produit.dto.GetDTO;
import com.example.ms_produit.dto.GetDTOPublic;
import com.example.ms_produit.dto.PostDTO;
import com.example.ms_produit.dto.PutDTO;
import com.example.ms_produit.exception.DuplicateProduitException;
import com.example.ms_produit.exception.FournisseurAllReadyAssociatedException;
import com.example.ms_produit.exception.FournisseurNotFoundException;
import com.example.ms_produit.exception.ProduitNotFoundException;
import com.example.ms_produit.mapper.Mapper;
import com.example.ms_produit.model.Produit;
import com.example.ms_produit.proxy.Ms_Fournisseur;
import com.example.ms_produit.repository.ProduitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProduitService {
    @Autowired
    private ProduitRepo repo;
    @Autowired
    private Mapper mapper;
    @Autowired
    private Ms_Fournisseur msFournisseur;
    public List<GetDTO> getAllProduit(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }
    public GetDTO getProduitById(Long id){
        return repo.findById(id).map(Mapper::toDTO).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
    }
    public GetDTO getProduitByName(String nom){
        return repo.findByNom(nom).map(Mapper::toDTO).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
    }
    public GetDTOPublic getDTOPublic(Long id){
        return repo.findById(id).map(Mapper::getDTOPublic).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
    }
    public GetDTO createProduit(PostDTO postDTO){
        if(repo.existsByNom(postDTO.getNom())) throw new DuplicateProduitException("Product All Ready Exist.");
        if(postDTO.getFournisseurIds() == null) postDTO.setFournisseurIds(new ArrayList<>());
        Produit produit = repo.save(mapper.getToEntity(postDTO));
        return Mapper.toDTO(produit);
    }
    public GetDTO updateProduit(Long id,PutDTO putDTO){
        Produit produit = repo.findById(id).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
        produit.setUrlImage(putDTO.getUrlImage());
        produit.setPrix(putDTO.getPrix());
        Produit saved = repo.save(produit);
        return Mapper.toDTO(saved);
    }
    public void deleteproduit(Long id){
        if(!repo.existsById(id)) throw new ProduitNotFoundException("Product Not Found .");
        repo.deleteById(id);
    }
    // Recuperer tout les produits d'un fournisseur
    public List<GetDTOPublic> getProduitByFournisseur(Long id){
        FournisseurBean fournisseurBean = msFournisseur.getPublicFournisseur(id);
        if(fournisseurBean == null) throw new FournisseurNotFoundException("This supplier not found");
        return repo.findAll().stream().filter(produit -> produit.getFournisseurIds() != null && produit.getFournisseurIds().contains(id)).map(Mapper::getDTOPublic).toList();
    }
    // Verification est ce que le produit apartient a un fournisseur
    public boolean isProduitDisponibleChezFournisseur(Long idProduit,Long idFournisseur){
        Produit produit = repo.findById(idProduit).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
        FournisseurBean fournisseurBean = msFournisseur.getPublicFournisseur(idFournisseur);
        if(fournisseurBean == null) throw new  FournisseurNotFoundException("This supplier not found");
        return produit.getFournisseurIds() != null && produit.getFournisseurIds().contains(idFournisseur);
    }
    public void associerFournisseur(Long idProduit,Long idFournisseur){
        Produit produit = repo.findById(idProduit).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
        if(msFournisseur.getPublicFournisseur(idFournisseur) == null) throw new FournisseurNotFoundException("This supplier not found");
        if(produit.getFournisseurIds() == null) produit.setFournisseurIds(new ArrayList<>());
        if(isProduitDisponibleChezFournisseur(idProduit, idFournisseur)) throw new FournisseurAllReadyAssociatedException("This supplier is already associated with this product");
        produit.getFournisseurIds().add(idFournisseur);
        repo.save(produit);
    }
    public void dissocierFournisseur(Long idProduit,Long idFournisseur){
        Produit produit = repo.findById(idProduit).orElseThrow(()-> new ProduitNotFoundException("Product Not Found ."));
        if(produit.getFournisseurIds() == null || !produit.getFournisseurIds().contains(idFournisseur)) throw new FournisseurNotFoundException("This supplier not found");
        produit.getFournisseurIds().remove(idFournisseur);
        repo.save(produit);
    }
    public List<FournisseurBean> getFournisseursByProduit(Long idProduit) {
        Produit produit = repo.findById(idProduit)
                .orElseThrow(() -> new ProduitNotFoundException("Product Not Found."));
        if (produit.getFournisseurIds() == null || produit.getFournisseurIds().isEmpty()) {
            return new ArrayList<>();
        }
        // Récupérer les détails des fournisseurs depuis le microservice
        List<FournisseurBean> fournisseurs = new ArrayList<>();
        for (Long fournisseurId : produit.getFournisseurIds()) {
            FournisseurBean fournisseur = msFournisseur.getPublicFournisseur(fournisseurId);
            if (fournisseur != null) {
                fournisseurs.add(fournisseur);
            }
        }
        return fournisseurs;
    }
}
