package com.example.ms_produit.proxy;

import com.example.ms_produit.bean.FournisseurBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "fournisseur-service")
public interface Ms_Fournisseur {
    @GetMapping("/api/fournisseur/public/{id}")
    public FournisseurBean getPublicFournisseur(@PathVariable Long id);
}
