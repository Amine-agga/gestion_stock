package com.example.ms_commande.service;

import com.example.ms_commande.bean.FournisseurBean;
import com.example.ms_commande.bean.ProduitBean;
import com.example.ms_commande.bean.StockBean;
import com.example.ms_commande.bean.StockItemsBean;
import com.example.ms_commande.dto.*;
import com.example.ms_commande.service.EmailService;
import com.example.ms_commande.exception.*;
import com.example.ms_commande.mapper.Mapper;
import com.example.ms_commande.model.Commande;
import com.example.ms_commande.model.CommandeStatus;
import com.example.ms_commande.model.LigneCommande;
import com.example.ms_commande.proxy.Ms_Stock;
import com.example.ms_commande.proxy.Ms_fournisseur;
import com.example.ms_commande.proxy.Ms_produit;
import com.example.ms_commande.repository.CommandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CommandeService {
    @Autowired
    private Ms_fournisseur msFournisseur;
    @Autowired
    private Ms_produit msProduit;
    @Autowired
    private Ms_Stock msStock;
    @Autowired
    private CommandeRepository repository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private EmailService emailService;
    private String DEFAULT_USER="SYSTEM";

    public List<CommandeResponseDTO> getAllCommandes(){
        return repository.findAllWithLignes().stream().map(Mapper::toResponseDTO).toList();
    }

    public CommandeResponseDTO getCommandeById(Long id){
        return repository.findByIdWithLignes(id).map(Mapper::toResponseDTO).orElseThrow(()-> new CommandeNotFoundException("Order not found."));
    }

    public List<CommandeResponseDTO> getCommandeByFournisseur(Long idfournisseur){
        return repository.findByIdfournisseur(idfournisseur).stream().map(Mapper::toResponseDTO).toList();
    }

    public List<CommandeResponseDTO> getCommandesByStatut(CommandeStatus commandeStatus){
        return repository.findByCommandeStatus(commandeStatus).stream().map(Mapper::toResponseDTO).toList();
    }

    public CommandeResponseDTO getCommandeByReference(String reference){
        return repository.findByReference(reference).map(Mapper::toResponseDTO).orElseThrow(()->new CommandeNotFoundException("Order not found.") );
    }

    @Transactional
    public CommandeResponseDTO createCommande(CommandeRequestDTO commandeRequestDTO){
        FournisseurBean fournisseurBean = msFournisseur.getPublicFournisseur(commandeRequestDTO.getIdfournisseur());
        // Verifier est ce que le fournisseur existe
        if(fournisseurBean == null) throw new FournisseurNotFoundException("Supplier Not found.");
        // Verifier est ce que le stock existe
        StockBean stockBean = msStock.getStock(commandeRequestDTO.getIdStock());
        if(stockBean == null) throw new StockNotFoundException("Stock Not Found.");

        // La creation d'un nouvelle commande
        Commande commande = new Commande();
        if(commandeRequestDTO.getRealisateur() == null || commandeRequestDTO.getRealisateur().trim().isEmpty())
            commande.setRealisateur(DEFAULT_USER);
        else commande.setRealisateur(commandeRequestDTO.getRealisateur().trim());

        commande.setReference(genererReference());
        commande.setIdfournisseur(commandeRequestDTO.getIdfournisseur());
        commande.setFournisseurEmail(fournisseurBean.getEmail());
        commande.setFournisseurNom(fournisseurBean.getNom());
        commande.setIdStock(commandeRequestDTO.getIdStock());
        commande.setStockNom(stockBean.getNom());
        commande.setCommandeStatus(CommandeStatus.EN_ATTENTE);
        commande.setDateCreation(new Date());
        commande.setLigneCommandes(new ArrayList<>());

        // 🆕 GÉNÉRATION DU TOKEN DE VALIDATION
        String validationToken = UUID.randomUUID().toString();
        commande.setValidationToken(validationToken);

        // Token valide pendant 7 jours
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        commande.setTokenExpiration(calendar.getTime());

        double montantTotal = 0.0;
        for(LigneCommandeRequestDTO ligneDTO : commandeRequestDTO.getLignes()){
            // Verifier est ce le produit existe
            ProduitBean produitBean = msProduit.getDTOPublic(ligneDTO.getIdProduit());
            if(produitBean == null) throw  new ProduitNotFoundException("Product Not Found");
            // Verifier est ce le produit appartient a cette fournisseur
            Boolean disponible = msProduit.isProduitDisponibleChezFournisseur(ligneDTO.getIdProduit(), commandeRequestDTO.getIdfournisseur());
            if(!disponible) throw new NotAssocietedProductException("This supplier does not sell this product.");
            // La creation d'un nouvelle ligne de commande
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setIdProduit(ligneDTO.getIdProduit());
            ligneCommande.setPrix(produitBean.getPrix());
            ligneCommande.setQuantite(ligneDTO.getQuantite());
            ligneCommande.setProduitNom(produitBean.getNom());
            ligneCommande.setSousTotal(produitBean.getPrix()*ligneDTO.getQuantite());
            montantTotal+= ligneCommande.getSousTotal();
            // L'ajout du ligne de commande dans la commande
            commande.addLigne(ligneCommande);
        }
        commande.setMontantTotal(montantTotal);
        Commande saved = repository.save(commande);

        // 🆕 ENVOI DE L'EMAIL AVEC LE LIEN DE VALIDATION
        emailService.envoyerEmailNouvelleCommande(saved);

        return Mapper.toResponseDTO(saved);
    }

    // 🆕 NOUVELLE MÉTHODE : VALIDATION PAR TOKEN (DEPUIS L'EMAIL)
    @Transactional
    public CommandeResponseDTO validerCommandeParToken(String token) {
        Commande commande = repository.findByValidationToken(token)
                .orElseThrow(() -> new CommandeNotFoundException("Invalid validation token"));

        // Vérifier l'expiration du token
        if(commande.getTokenExpiration().before(new Date())) {
            throw new TokenExpiredException("Validation link has expired");
        }

        // Vérifier que la commande est bien en attente
        if(commande.getCommandeStatus() != CommandeStatus.EN_ATTENTE) {
            throw new CommandeStatusException("Order has already been processed");
        }

        // Valider la commande
        commande.setCommandeStatus(CommandeStatus.VALIDEE);
        commande.setDateValidation(new Date());
        commande.setValidationToken(null); // Invalider le token après utilisation

        Commande validee = repository.save(commande);

        // Optionnel : envoyer un email de confirmation
        emailService.envoyerEmailCommandeValidee(validee);

        return Mapper.toResponseDTO(validee);
    }

    @Transactional
    public CommandeResponseDTO updateCommande(Long id,CommandeUpdateDTO commandeUpdateDTO){
        Commande commande = repository.findByIdWithLignes(id).orElseThrow(()-> new CommandeNotFoundException("Order not found."));
        if(!commande.peutEtreModifiee()) throw new ImpossibleUpdateException("This order cannot be modified because it has already been delivered.");
        // Verifier est ce que c'est un nouveau fournisseur de commande
        if(commandeUpdateDTO.getIdfournisseur() != null && !commandeUpdateDTO.getIdfournisseur().equals(commande.getIdfournisseur())){
            FournisseurBean fournisseurBean = msFournisseur.getPublicFournisseur(commandeUpdateDTO.getIdfournisseur());
            if(fournisseurBean == null) throw new FournisseurNotFoundException("This supplier does not sell this product.");
            commande.setIdfournisseur(commandeUpdateDTO.getIdfournisseur());
            commande.setFournisseurEmail(fournisseurBean.getEmail());
            commande.setFournisseurNom(fournisseurBean.getNom());
        }
        // Verifier est ce que le stock est ce que c'est un nouveau stock
        if(commandeUpdateDTO.getIdStock() != null && !commandeUpdateDTO.getIdStock().equals(commande.getIdStock())){
            StockBean stockBean = msStock.getStock(commandeUpdateDTO.getIdStock());
            if(stockBean == null) throw new StockNotFoundException("Stock Not Found.");
            commande.setStockNom(stockBean.getNom());
            commande.setIdStock(stockBean.getId());
        }
        // Verifier est ce que des nouveaux ligne de commandes
        if(commandeUpdateDTO.getLignes() != null && !commandeUpdateDTO.getLignes().isEmpty()){
            commande.getLigneCommandes().clear();
            double montantTotal = 0.0;
            for(LigneCommandeRequestDTO ligneCommandeRequestDTO : commandeUpdateDTO.getLignes()){
                // Verifier l'existance du produit
                ProduitBean produitBean = msProduit.getDTOPublic(ligneCommandeRequestDTO.getIdProduit());
                if(produitBean == null) throw  new ProduitNotFoundException("Product Not Found");
                // Verifier est ce que ce produit founit par ce fournisseur
                Boolean disponible = msProduit.isProduitDisponibleChezFournisseur(ligneCommandeRequestDTO.getIdProduit(), commande.getIdfournisseur());
                if(disponible == null || !disponible) throw new NotAssocietedProductException("This supplier does not sell this product.");
                // La creation d'une nouvelle ligne de commande
                LigneCommande ligneCommande = new LigneCommande();
                ligneCommande.setIdProduit(produitBean.getId());
                ligneCommande.setProduitNom(produitBean.getNom());
                ligneCommande.setQuantite(ligneCommandeRequestDTO.getQuantite());
                ligneCommande.setPrix(produitBean.getPrix());
                ligneCommande.setSousTotal(ligneCommandeRequestDTO.getQuantite() * produitBean.getPrix());
                montantTotal+= ligneCommande.getSousTotal();
                commande.addLigne(ligneCommande);
            }
            commande.setMontantTotal(montantTotal);
        }
        Commande updated = repository.save(commande);
        return Mapper.toResponseDTO(updated);
    }

    @Transactional
    public CommandeResponseDTO marquerCommeLivree(Long id){
        Commande commande = repository.findByIdWithLignes(id)
                .orElseThrow(()-> new CommandeNotFoundException("Order Not Found"));
        if(commande.getCommandeStatus() != CommandeStatus.EN_COURS){
            throw new CommandeStatusException("The order must be in progress before it can be marked as delivered.");
        }
        commande.setCommandeStatus(CommandeStatus.LIVREE);
        commande.setDateReception(new Date());
        // Créditer le stock pour chaque ligne de commande
        for(LigneCommande ligneCommande : commande.getLigneCommandes()) {
            StockItemsBean stockItemsBean = msStock.crediterStock(
                    commande.getIdStock(),
                    ligneCommande.getIdProduit(),
                    ligneCommande.getQuantite(),
                    commande.getRealisateur()
            );
            if (stockItemsBean == null) {
                System.out.println("ERREUR: stockItemsBean est null !");
                throw new ImpossibleCrediterException(
                        "Unable to credit product " + ligneCommande.getIdProduit()
                );
            }
        }
        Commande livree = repository.save(commande);
        emailService.envoyerEmailCommandeLivree(livree);
        return Mapper.toResponseDTO(livree);
    }

    @Transactional
    public void deleteCommande(Long id) {
        Commande commande = repository.findByIdWithLignes(id)
                .orElseThrow(() -> new CommandeNotFoundException("Order Not Found"));
        if (!commande.peutEtreSupprimee()) {
            throw new ImpossibleSupprimerCommandeException(
                    "This order cannot be deleted. Status: " + commande.getCommandeStatus());
        }
        commande.getLigneCommandes().clear();
        repository.delete(commande);
    }

    @Transactional
    public CommandeResponseDTO validerCommande(Long id){
        Commande commande = repository.findById(id).orElseThrow(() -> new CommandeNotFoundException("Order Not Found"));
        if(commande.getCommandeStatus() == CommandeStatus.EN_ATTENTE) commande.setCommandeStatus(CommandeStatus.VALIDEE);
        else throw new CommandeStatusException("The order must be in pending status before it can be validated.");
        Commande valider = repository.save(commande);
        emailService.envoyerEmailCommandeValidee(valider);
        return Mapper.toResponseDTO(valider);
    }

    @Transactional
    public CommandeResponseDTO marquerEncours(Long id){
        Commande commande = repository.findById(id).orElseThrow(() -> new CommandeNotFoundException("Order Not Found"));
        if(commande.getCommandeStatus() == CommandeStatus.VALIDEE) commande.setCommandeStatus(CommandeStatus.EN_COURS);
        else throw new CommandeStatusException("This order must be validated in order to move it to in-progress status.");
        Commande encours = repository.save(commande);
        return Mapper.toResponseDTO(encours);
    }

    @Transactional
    public CommandeResponseDTO annulerCommande(Long id,String motif){
        Commande commande = repository.findById(id).orElseThrow(() -> new CommandeNotFoundException("Order Not Found"));
        if(commande.getCommandeStatus() != CommandeStatus.LIVREE){
            commande.setCommandeStatus(CommandeStatus.ANNULEE);
            commande.setMotifAnnulation(motif);
        }
        else throw new CommandeStatusException("Cannot cancel an order that has already been delivered.");
        Commande annuler = repository.save(commande);
        return Mapper.toResponseDTO(annuler);
    }

    private String genererReference() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String annee = simpleDateFormat.format(new Date());
        String lastReference = repository.findTopByReferenceLikeOrderByReferenceDesc("CMD-" + annee + "%")
                .map(Commande::getReference)
                .orElse("CMD-" + annee + "-000000");
        int lastNumber = Integer.parseInt(lastReference.substring(lastReference.length() - 6));
        int nextNumber = lastNumber + 1;

        return String.format("CMD-%s-%06d", annee, nextNumber);
    }
}