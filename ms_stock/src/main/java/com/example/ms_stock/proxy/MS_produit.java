package com.example.ms_stock.proxy;

import com.example.ms_stock.bean.ProduitBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-produit")
public interface MS_produit {
    @GetMapping("/api/produits/{id}/public")  // Ajoutez le base path du controller
    public ProduitBean getDTOPublic(@PathVariable Long id);
}
