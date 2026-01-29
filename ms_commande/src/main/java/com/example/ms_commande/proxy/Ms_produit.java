package com.example.ms_commande.proxy;

import com.example.ms_commande.bean.ProduitBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-produit" , fallback = ProduitBean.class)
public interface Ms_produit {
    @GetMapping("/api/produits/{id}/public")
    public ProduitBean getDTOPublic(@PathVariable Long id);
    @GetMapping("/api/produits/fournisseur/{id}")
    public List<ProduitBean> getProduitByfournisseur(@PathVariable Long id);
    @GetMapping("/api/produits/{idProduit}/fournisseur/{idFournisseur}")
    public Boolean isProduitDisponibleChezFournisseur(@PathVariable Long idProduit,@PathVariable Long idFournisseur);
}
