package com.example.ms_commande.controller;

import com.example.ms_commande.dto.CommandeRequestDTO;
import com.example.ms_commande.dto.CommandeResponseDTO;
import com.example.ms_commande.dto.CommandeUpdateDTO;
import com.example.ms_commande.service.CommandeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {
    @Autowired
    private CommandeService commandeService;
    @GetMapping
    public ResponseEntity<List<CommandeResponseDTO>> getAllCommandes(){
        return ResponseEntity.ok(commandeService.getAllCommandes());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> getCommandeById(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.getCommandeById(id));
    }
    @GetMapping("/fournisseur/{idFournisseur}")
    public ResponseEntity<List<CommandeResponseDTO>> getAllCommandeByFournisseur(@PathVariable Long idFournisseur){
        return ResponseEntity.ok(commandeService.getCommandeByFournisseur(idFournisseur));
    }
    @GetMapping("/reference/{reference}")
    public ResponseEntity<CommandeResponseDTO> getCommandeByReference(@PathVariable String reference){
        return ResponseEntity.ok(commandeService.getCommandeByReference(reference));
    }
    @PostMapping
    public ResponseEntity<CommandeResponseDTO> createCommande(@Valid @RequestBody CommandeRequestDTO commandeRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.createCommande(commandeRequestDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponseDTO> updateCommande(@PathVariable Long id, @Valid @RequestBody CommandeUpdateDTO commandeUpdateDTO){
        return ResponseEntity.ok(commandeService.updateCommande(id,commandeUpdateDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id){
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/valider")
    public ResponseEntity<CommandeResponseDTO> validerCommande(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.validerCommande(id));
    }
    @PatchMapping("/{id}/en-cours")
    public ResponseEntity<CommandeResponseDTO> marquerEncours(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.marquerEncours(id));
    }
    @PatchMapping("/{id}/livrer")
    public ResponseEntity<CommandeResponseDTO> marquerCommeLivree(@PathVariable Long id){
        return ResponseEntity.ok(commandeService.marquerCommeLivree(id));
    }
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<CommandeResponseDTO> annulerCommande(@PathVariable Long id,@RequestParam String motif){
        return ResponseEntity.ok(commandeService.annulerCommande(id,motif));
    }
    // 🆕 ENDPOINT PUBLIC : VALIDATION PAR EMAIL (sans JWT)
    /**
     * Endpoint public permettant au fournisseur de valider une commande via email
     * L'URL sera : http://localhost:8080/api/commandes/valider-email/{token}
     * Cet endpoint ne nécessite PAS d'authentification JWT
     */
    @GetMapping("/valider-email/{token}")
    public ResponseEntity<String> validerParEmail(@PathVariable String token) {
        try {
            CommandeResponseDTO commande = commandeService.validerCommandeParToken(token);

            // Retourner une page HTML de confirmation
            String html = construirePageConfirmation(commande);
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(html);

        } catch (Exception e) {
            // Page d'erreur HTML
            String errorHtml = construirePageErreur(e.getMessage());
            return ResponseEntity.badRequest()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(errorHtml);
        }
    }

    /**
     * Construit une page HTML de confirmation de validation
     */
    private String construirePageConfirmation(CommandeResponseDTO commande) {
        return String.format("""
            <!DOCTYPE html>
            <html lang='fr'>
            <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1.0'>
                <title>Commande Validée</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        background: white;
                        border-radius: 15px;
                        box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                        max-width: 600px;
                        width: 100%%;
                        padding: 40px;
                        text-align: center;
                    }
                    .success-icon {
                        width: 80px;
                        height: 80px;
                        background: #28a745;
                        border-radius: 50%%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        margin: 0 auto 20px;
                        font-size: 3em;
                        color: white;
                    }
                    h1 { color: #28a745; margin: 20px 0; }
                    .info-box {
                        background: #f8f9fa;
                        border-radius: 10px;
                        padding: 20px;
                        margin: 20px 0;
                        text-align: left;
                    }
                    .info-row {
                        display: flex;
                        justify-content: space-between;
                        padding: 10px 0;
                        border-bottom: 1px solid #dee2e6;
                    }
                    .info-row:last-child { border-bottom: none; }
                    .label { font-weight: bold; color: #495057; }
                    .value { color: #212529; }
                    .message { color: #6c757d; line-height: 1.6; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='success-icon'>✓</div>
                    <h1>✅ Commande Validée avec Succès!</h1>
                    <p class='message'>La commande a été validée et est maintenant prête à être traitée.</p>
                    <div class='info-box'>
                        <div class='info-row'>
                            <span class='label'>📋 Référence:</span>
                            <span class='value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='label'>🏢 Fournisseur:</span>
                            <span class='value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='label'>📦 Stock:</span>
                            <span class='value'>%s</span>
                        </div>
                        <div class='info-row'>
                            <span class='label'>💰 Montant Total:</span>
                            <span class='value'>%.2f MAD</span>
                        </div>
                        <div class='info-row'>
                            <span class='label'>📊 Statut:</span>
                            <span class='value' style='color: #28a745; font-weight: bold;'>%s</span>
                        </div>
                    </div>
                    <p class='message'>🎉 Merci d'avoir validé cette commande. Vous recevrez un email de confirmation sous peu.</p>
                </div>
            </body>
            </html>
            """,
                commande.getReference(),
                commande.getFournisseurNom(),
                commande.getStockNom(),
                commande.getMontantTotal(),
                commande.getCommandeStatus()
        );
    }

    /**
     * Construit une page HTML d'erreur
     */
    private String construirePageErreur(String message) {
        return String.format("""
            <!DOCTYPE html>
            <html lang='fr'>
            <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1.0'>
                <title>Erreur de Validation</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #f093fb 0%%, #f5576c 100%%);
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        background: white;
                        border-radius: 15px;
                        box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                        max-width: 600px;
                        width: 100%%;
                        padding: 40px;
                        text-align: center;
                    }
                    .error-icon {
                        width: 80px;
                        height: 80px;
                        background: #dc3545;
                        border-radius: 50%%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        margin: 0 auto 20px;
                        color: white;
                        font-size: 3em;
                    }
                    h1 { color: #dc3545; }
                    .error-message {
                        background: #f8d7da;
                        border: 1px solid #f5c6cb;
                        border-radius: 10px;
                        padding: 20px;
                        color: #721c24;
                        margin: 20px 0;
                    }
                    .suggestions {
                        text-align: left;
                        margin: 20px 0;
                        color: #6c757d;
                    }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='error-icon'>✕</div>
                    <h1>❌ Erreur de Validation</h1>
                    <div class='error-message'>
                        <strong>Impossible de valider la commande</strong><br>
                        %s
                    </div>
                    <div class='suggestions'>
                        <p><strong>Raisons possibles :</strong></p>
                        <ul>
                            <li>Le lien de validation a expiré (valide 7 jours)</li>
                            <li>La commande a déjà été validée</li>
                            <li>Le lien est invalide</li>
                        </ul>
                        <p>💡 Veuillez contacter l'administrateur.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                message
        );
    }
}
