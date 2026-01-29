package com.example.ms_commande.proxy;

import com.example.ms_commande.bean.FournisseurBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "fournisseur-service")
public interface Ms_fournisseur {
    @GetMapping("/api/fournisseur/public/{id}")
    public FournisseurBean getPublicFournisseur(@PathVariable("id") Long id);
}
