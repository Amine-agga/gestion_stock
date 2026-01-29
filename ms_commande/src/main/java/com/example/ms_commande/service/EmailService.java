package com.example.ms_commande.service;

import com.example.ms_commande.model.Commande;
import com.example.ms_commande.model.LigneCommande;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${app.admin.email:admin@example.com}")
    private String adminEmail;

    // URL de base pour la validation (à configurer dans application.properties)
    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    public void envoyerEmailNouvelleCommande(Commande commande) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(commande.getFournisseurEmail());
            helper.setSubject("Nouvelle Commande - " + commande.getReference());
            helper.setText(construireEmailNouvelleCommande(commande), true);

            mailSender.send(message);
            System.out.println("✅ Email envoyé à : " + commande.getFournisseurEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Erreur envoi email: " + e.getMessage());
            // Ne pas bloquer le processus si l'email échoue
        }
    }

    /**
     * Envoie un email au fournisseur lors de la validation d'une commande
     */
    public void envoyerEmailCommandeValidee(Commande commande) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(commande.getFournisseurEmail());
            helper.setSubject("Commande Validée - " + commande.getReference());
            helper.setText(construireEmailCommandeValidee(commande), true);

            mailSender.send(message);
            System.out.println("✅ Email de validation envoyé à : " + commande.getFournisseurEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Erreur envoi email: " + e.getMessage());
        }
    }

    /**
     * Envoie un email à l'admin lors de la livraison d'une commande
     */
    public void envoyerEmailCommandeLivree(Commande commande) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject("Commande Livrée - " + commande.getReference());
            helper.setText(construireEmailCommandeLivree(commande), true);

            mailSender.send(message);
            System.out.println("✅ Email de livraison envoyé à l'admin");
        } catch (MessagingException e) {
            System.err.println("❌ Erreur envoi email: " + e.getMessage());
        }
    }

    /**
     * Construit le contenu HTML de l'email pour une nouvelle commande
     * AVEC BOUTON DE VALIDATION
     */
    private String construireEmailNouvelleCommande(Commande commande) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Construction de l'URL de validation
        String validationUrl = baseUrl + "/api/commandes/valider-email/" + commande.getValidationToken();

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='fr'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #714b67, #9a7c94); color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border: 1px solid #ddd; }");
        html.append(".info-box { background: white; padding: 15px; margin: 15px 0; border-left: 4px solid #714b67; border-radius: 5px; }");
        html.append(".info-label { font-weight: bold; color: #714b67; }");
        html.append(".table { width: 100%; border-collapse: collapse; margin: 20px 0; background: white; }");
        html.append(".table th { background: #714b67; color: white; padding: 12px; text-align: left; }");
        html.append(".table td { padding: 10px; border-bottom: 1px solid #ddd; }");
        html.append(".total { font-size: 1.2em; font-weight: bold; color: #714b67; text-align: right; padding: 15px; background: #f0f0f0; }");
        html.append(".footer { text-align: center; padding: 20px; color: #666; font-size: 0.9em; }");
        html.append(".status { display: inline-block; padding: 5px 15px; background: #ffc107; color: #856404; border-radius: 20px; font-weight: bold; }");

        // Styles pour le bouton de validation
        html.append(".validation-section { background: linear-gradient(135deg, #28a745, #5cb85c); padding: 25px; margin: 25px 0; border-radius: 10px; text-align: center; }");
        html.append(".validation-title { color: white; font-size: 1.3em; margin-bottom: 15px; font-weight: bold; }");
        html.append(".btn-valider { display: inline-block; padding: 15px 40px; background: white; color: #28a745; text-decoration: none; font-weight: bold; border-radius: 30px; font-size: 1.1em; box-shadow: 0 4px 8px rgba(0,0,0,0.2); transition: all 0.3s; }");
        html.append(".btn-valider:hover { transform: translateY(-2px); box-shadow: 0 6px 12px rgba(0,0,0,0.3); }");
        html.append(".warning-box { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 15px 0; border-radius: 5px; }");

        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // En-tête
        html.append("<div class='header'>");
        html.append("<h1>📦 Nouvelle Commande</h1>");
        html.append("<p>Référence: ").append(commande.getReference()).append("</p>");
        html.append("</div>");

        // Contenu
        html.append("<div class='content'>");

        // Message de bienvenue
        html.append("<p>Bonjour <strong>").append(commande.getFournisseurNom()).append("</strong>,</p>");
        html.append("<p>Nous avons le plaisir de vous informer qu'une nouvelle commande a été créée.</p>");

        // SECTION DE VALIDATION - LA NOUVELLE PARTIE !
        html.append("<div class='validation-section'>");
        html.append("<div class='validation-title'>🎯 Action Requise</div>");
        html.append("<p style='color: white; margin: 10px 0;'>Veuillez valider cette commande en cliquant sur le bouton ci-dessous :</p>");
        html.append("<a href='").append(validationUrl).append("' class='btn-valider'>✅ VALIDER LA COMMANDE</a>");
        html.append("<p style='color: white; font-size: 0.9em; margin-top: 15px;'>⏰ Ce lien est valide pendant 7 jours</p>");
        html.append("</div>");

        // Informations de la commande
        html.append("<div class='info-box'>");
        html.append("<p><span class='info-label'>📋 Référence:</span> ").append(commande.getReference()).append("</p>");
        html.append("<p><span class='info-label'>📅 Date de création:</span> ").append(sdf.format(commande.getDateCreation())).append("</p>");
        html.append("<p><span class='info-label'>🏢 Stock de destination:</span> ").append(commande.getStockNom()).append("</p>");
        html.append("<p><span class='info-label'>📊 Statut:</span> <span class='status'>").append(commande.getCommandeStatus()).append("</span></p>");
        html.append("</div>");

        // Détails des produits
        html.append("<h3>📦 Détails de la commande</h3>");
        html.append("<table class='table'>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Produit</th>");
        html.append("<th>Quantité</th>");
        html.append("<th>Prix unitaire</th>");
        html.append("<th>Sous-total</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        for (LigneCommande ligne : commande.getLigneCommandes()) {
            html.append("<tr>");
            html.append("<td>").append(ligne.getProduitNom()).append("</td>");
            html.append("<td>").append(ligne.getQuantite()).append("</td>");
            html.append("<td>").append(String.format("%.2f MAD", ligne.getPrix())).append("</td>");
            html.append("<td>").append(String.format("%.2f MAD", ligne.getSousTotal())).append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        // Montant total
        html.append("<div class='total'>");
        html.append("💰 Montant Total: ").append(String.format("%.2f MAD", commande.getMontantTotal()));
        html.append("</div>");

        // Note importante
        html.append("<div class='warning-box'>");
        html.append("<p><strong>⚠️ Important :</strong></p>");
        html.append("<ul>");
        html.append("<li>Cliquez sur le bouton de validation pour confirmer la commande</li>");
        html.append("<li>Après validation, la commande passera automatiquement au statut VALIDÉE</li>");
        html.append("<li>Le lien de validation expire dans 7 jours</li>");
        html.append("</ul>");
        html.append("</div>");

        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Ceci est un email automatique, merci de ne pas y répondre.</p>");
        html.append("<p>© 2024 Système de Gestion de Stock</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Construit le contenu HTML de l'email pour une commande validée
     */
    private String construireEmailCommandeValidee(Commande commande) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='fr'>");
        html.append("<head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #28a745, #5cb85c); color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border: 1px solid #ddd; }");
        html.append(".success-box { background: #d4edda; border-left: 4px solid #28a745; padding: 15px; margin: 15px 0; border-radius: 5px; }");
        html.append(".footer { text-align: center; padding: 20px; color: #666; font-size: 0.9em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        html.append("<div class='header'>");
        html.append("<h1>✅ Commande Validée</h1>");
        html.append("<p>").append(commande.getReference()).append("</p>");
        html.append("</div>");

        html.append("<div class='content'>");
        html.append("<p>Bonjour <strong>").append(commande.getFournisseurNom()).append("</strong>,</p>");
        html.append("<div class='success-box'>");
        html.append("<h3>🎉 Bonne nouvelle !</h3>");
        html.append("<p>Votre commande <strong>").append(commande.getReference()).append("</strong> a été validée et est maintenant prête à être traitée.</p>");
        html.append("<p><strong>Montant total:</strong> ").append(String.format("%.2f MAD", commande.getMontantTotal())).append("</p>");
        html.append("</div>");
        html.append("<p>Merci de préparer les produits commandés pour la livraison.</p>");
        html.append("</div>");

        html.append("<div class='footer'>");
        html.append("<p>© 2024 Système de Gestion de Stock</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Construit le contenu HTML de l'email pour une commande livrée (admin)
     */
    private String construireEmailCommandeLivree(Commande commande) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='fr'>");
        html.append("<head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #17a2b8, #5bc0de); color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border: 1px solid #ddd; }");
        html.append(".info-box { background: white; padding: 15px; margin: 15px 0; border-left: 4px solid #17a2b8; border-radius: 5px; }");
        html.append(".footer { text-align: center; padding: 20px; color: #666; font-size: 0.9em; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        html.append("<div class='header'>");
        html.append("<h1>🚚 Commande Livrée</h1>");
        html.append("<p>").append(commande.getReference()).append("</p>");
        html.append("</div>");

        html.append("<div class='content'>");
        html.append("<p>Bonjour Admin,</p>");
        html.append("<p>La commande <strong>").append(commande.getReference()).append("</strong> a été livrée avec succès.</p>");

        html.append("<div class='info-box'>");
        html.append("<p><strong>🏢 Fournisseur:</strong> ").append(commande.getFournisseurNom()).append("</p>");
        html.append("<p><strong>📦 Stock:</strong> ").append(commande.getStockNom()).append("</p>");
        html.append("<p><strong>💰 Montant:</strong> ").append(String.format("%.2f MAD", commande.getMontantTotal())).append("</p>");
        html.append("<p><strong>📅 Date de réception:</strong> ").append(sdf.format(commande.getDateReception())).append("</p>");
        html.append("</div>");

        html.append("<p>Le stock a été automatiquement crédité.</p>");
        html.append("</div>");

        html.append("<div class='footer'>");
        html.append("<p>© 2025 STOCK FLOW</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}