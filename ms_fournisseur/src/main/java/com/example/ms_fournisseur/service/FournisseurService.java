package com.example.ms_fournisseur.service;

import com.example.ms_fournisseur.dto.GetFrounisseur;
import com.example.ms_fournisseur.dto.GetPublicFournisseur;
import com.example.ms_fournisseur.dto.PostFournisseur;
import com.example.ms_fournisseur.dto.PutFournisseur;
import com.example.ms_fournisseur.exception.FournisseurNotFoundException;
import com.example.ms_fournisseur.exception.FournisseurEmailAllReadyExistException;
import com.example.ms_fournisseur.exception.FournisseurTelephoneAllReadyExistException;
import com.example.ms_fournisseur.mapper.Mapper;
import com.example.ms_fournisseur.model.Fournisseur;
import com.example.ms_fournisseur.repository.FournisseurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {
    @Autowired
    private FournisseurRepo repo;
    @Autowired
    private Mapper mapper;
    public GetFrounisseur createFournisseur(PostFournisseur postFournisseur){
        if(repo.existsByEmail(postFournisseur.getEmail())) throw new FournisseurEmailAllReadyExistException("This email is already used by another supplier.");
        if(repo.existsByTelephone(postFournisseur.getTelephone())) throw new FournisseurTelephoneAllReadyExistException("This phone number is already used by another supplier.");
        Fournisseur saved = repo.save(mapper.PostToEntity(postFournisseur));
        return mapper.EntityToDto(saved);
    }
    public GetFrounisseur getFrounisseur(Long id){
        return repo.findById(id).map(Mapper::EntityToDto).orElseThrow(()-> new FournisseurNotFoundException("Supplier not found."));
    }
    public List<GetFrounisseur> getFrounisseurs(){
        return repo.findAll().stream().map(Mapper::EntityToDto).toList();
    }
    public GetPublicFournisseur getPublicFournisseur(Long id){
        return repo.findById(id).map(Mapper::entityToPublic).orElseThrow(()-> new FournisseurNotFoundException("Supplier not found."));
    }
    public GetFrounisseur updateFounisseur(Long id, PutFournisseur putFournisseur){
        Fournisseur fournisseur = repo.findById(id).orElseThrow(()-> new FournisseurNotFoundException("Supplier not found."));
        /*if(repo.existsByEmail(putFournisseur.getEmail())) throw new FournisseurEmailAllReadyExistException("This email is already used by another supplier.");
        fournisseur.setEmail(putFournisseur.getEmail());
        fournisseur.setNom(putFournisseur.getNom());
        fournisseur.setPrenom(putFournisseur.getPrenom());*/
        fournisseur.setNom(putFournisseur.getNom());
        fournisseur.setPrenom(putFournisseur.getPrenom());
        if(!fournisseur.getEmail().equals(putFournisseur.getEmail())){
            Optional<Fournisseur> existByEmail = repo.findByEmail(putFournisseur.getEmail());
            if(existByEmail.isPresent() && !existByEmail.get().getId().equals(id)) throw new FournisseurEmailAllReadyExistException("This email is already used by another supplier.");
            fournisseur.setEmail(putFournisseur.getEmail());
        }
        if(!fournisseur.getTelephone().equals(putFournisseur.getTelephone())){
            Optional<Fournisseur> existByTelephone = repo.findByTelephone(putFournisseur.getTelephone());
            if(existByTelephone.isPresent() && !existByTelephone.get().getId().equals(id)) throw new FournisseurTelephoneAllReadyExistException("This phone number is already used by another supplier.");
            fournisseur.setTelephone(putFournisseur.getTelephone());
        }
        Fournisseur saved = repo.save(fournisseur);
        return mapper.EntityToDto(saved);
    }
    public void deleteFourniseur(Long id){
        if(!repo.existsById(id)) throw new FournisseurNotFoundException("Supplier not found.");
        repo.deleteById(id);
    }
}
