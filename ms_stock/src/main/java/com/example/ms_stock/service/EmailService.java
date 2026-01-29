package com.example.ms_stock.service;

import com.example.ms_stock.batch.model.StockAlerte;
import com.example.ms_stock.model.StockStatus;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${stock.alert.email.from:noreply@stock-management.com}")
    private String fromEmail;

    @Value("${stock.alert.email.recipients}")
    private String recipients;

    @Value("${stock.alert.email.enabled:true}")
    private boolean emailEnabled;

    /**
     * Envoie un email d'alerte pour un stock critique
     */
    public void sendStockCritiqueAlert(StockAlerte alerte) {
        if (!emailEnabled) {
            logger.info("📧 Envoi d'email désactivé dans la configuration");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipients.split(","));
            helper.setSubject(generateSubject(alerte));
            helper.setText(generateHtmlContent(alerte), true);

            mailSender.send(message);
            logger.info("✅ Email d'alerte envoyé pour le produit {} dans le stock '{}'",
                    alerte.getIdProduit(), alerte.getStockNom());

        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi de l'email d'alerte pour le produit {}: {}",
                    alerte.getIdProduit(), e.getMessage());
        }
    }

    /**
     * Envoie un email récapitulatif avec toutes les alertes du jour
     */
    public void sendDailyAlertSummary(List<StockAlerte> alertes) {
        if (!emailEnabled || alertes.isEmpty()) {
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipients.split(","));
            helper.setSubject("📊 Rapport Quotidien - Alertes de Stock (" + alertes.size() + " alerte(s))");
            helper.setText(generateSummaryHtmlContent(alertes), true);

            mailSender.send(message);
            logger.info("✅ Email récapitulatif envoyé avec {} alerte(s)", alertes.size());

        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'envoi de l'email récapitulatif: {}", e.getMessage());
            logger.error("Détails de l'erreur:", e);
        }
    }

    /**
     * Génère le sujet de l'email selon la gravité
     */
    private String generateSubject(StockAlerte alerte) {
        String icon = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "🔴" : "🟠";
        return String.format("%s ALERTE STOCK %s - Produit %d (%s)",
                icon,
                alerte.getStockStatus(),
                alerte.getIdProduit(),
                alerte.getStockNom());
    }

    /**
     * Génère le contenu HTML de l'email pour une alerte individuelle
     */
    private String generateHtmlContent(StockAlerte alerte) {
        String backgroundColor = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "#ffe5e5" : "#fff4e5";
        String statusColor = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "#d32f2f" : "#f57c00";
        String icon = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "🔴" : "🟠";

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .container {
                        background-color: %s;
                        border-left: 5px solid %s;
                        border-radius: 8px;
                        padding: 25px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    .header {
                        font-size: 24px;
                        font-weight: bold;
                        color: %s;
                        margin-bottom: 20px;
                        text-align: center;
                    }
                    .info-table {
                        width: 100%%;
                        border-collapse: collapse;
                        margin: 20px 0;
                    }
                    .info-table td {
                        padding: 12px;
                        border-bottom: 1px solid #ddd;
                    }
                    .info-table td:first-child {
                        font-weight: bold;
                        width: 40%%;
                        color: #555;
                    }
                    .status-badge {
                        display: inline-block;
                        padding: 6px 12px;
                        background-color: %s;
                        color: white;
                        border-radius: 4px;
                        font-weight: bold;
                        font-size: 14px;
                    }
                    .message-box {
                        background-color: white;
                        border-radius: 6px;
                        padding: 15px;
                        margin: 20px 0;
                        border: 1px solid #ddd;
                    }
                    .action-required {
                        background-color: #fff3cd;
                        border-left: 4px solid #ffc107;
                        padding: 15px;
                        margin: 20px 0;
                        border-radius: 4px;
                    }
                    .footer {
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 2px solid #ddd;
                        font-size: 12px;
                        color: #777;
                        text-align: center;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        %s ALERTE DE STOCK
                    </div>
                    
                    <table class="info-table">
                        <tr>
                            <td>Stock</td>
                            <td><strong>%s</strong> (ID: %d)</td>
                        </tr>
                        <tr>
                            <td>Produit</td>
                            <td><strong>ID: %d</strong></td>
                        </tr>
                        <tr>
                            <td>Quantité actuelle</td>
                            <td><strong style="color: %s; font-size: 18px;">%d unités</strong></td>
                        </tr>
                        <tr>
                            <td>Seuil minimum</td>
                            <td>%d unités</td>
                        </tr>
                        <tr>
                            <td>Statut</td>
                            <td><span class="status-badge">%s</span></td>
                        </tr>
                        <tr>
                            <td>Date de détection</td>
                            <td>%s</td>
                        </tr>
                    </table>
                    
                    <div class="message-box">
                        <strong>Message:</strong><br>
                        %s
                    </div>
                    
                    <div class="action-required">
                        <strong>⚠️ Action requise:</strong><br>
                        Veuillez vérifier le stock et <strong>passer une commande de réapprovisionnement</strong> 
                        dans les plus brefs délais pour éviter une rupture de stock.
                    </div>
                    
                    <div class="footer">
                        <p>Email automatique généré par le système de gestion de stock</p>
                        <p>Pour toute question, veuillez contacter le service logistique</p>
                        <p style="font-size: 10px; margin-top: 10px;">
                            Cet email a été envoyé par le batch de vérification quotidienne des stocks
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """,
                // Couleurs et styles
                backgroundColor, statusColor, statusColor, statusColor,
                // Contenu
                icon,
                alerte.getStockNom(), alerte.getStockId(),
                alerte.getIdProduit(),
                statusColor, alerte.getQuantite(),
                alerte.getSeuilMinimum(),
                alerte.getStockStatus(),
                alerte.getDateAlerte(),
                alerte.getMessage()
        );
    }

    /**
     * Génère le contenu HTML pour l'email récapitulatif
     */
    private String generateSummaryHtmlContent(List<StockAlerte> alertes) {
        StringBuilder tableRows = new StringBuilder();
        int ruptureCount = 0;
        int critiqueCount = 0;

        for (StockAlerte alerte : alertes) {
            String icon = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "🔴" : "🟠";
            String rowColor = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "#ffe5e5" : "#fff4e5";

            if (alerte.getStockStatus() == StockStatus.EN_RUPTURE) {
                ruptureCount++;
            } else {
                critiqueCount++;
            }

            tableRows.append(String.format("""
                <tr style="background-color: %s;">
                    <td style="padding: 12px; border-bottom: 1px solid #ddd;">%s</td>
                    <td style="padding: 12px; border-bottom: 1px solid #ddd;">%d</td>
                    <td style="padding: 12px; border-bottom: 1px solid #ddd; font-weight: bold;">%d</td>
                    <td style="padding: 12px; border-bottom: 1px solid #ddd;">%d</td>
                    <td style="padding: 12px; border-bottom: 1px solid #ddd;">%s %s</td>
                </tr>
                """,
                    rowColor,
                    alerte.getStockNom(),
                    alerte.getIdProduit(),
                    alerte.getQuantite(),
                    alerte.getSeuilMinimum(),
                    icon, alerte.getStockStatus()
            ));
        }

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 800px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                             color: white; padding: 30px; border-radius: 8px; text-align: center; }
                    .summary-box { display: flex; justify-content: space-around; margin: 20px 0; }
                    .stat-card { background: white; border-radius: 8px; padding: 20px; 
                                box-shadow: 0 2px 4px rgba(0,0,0,0.1); text-align: center; flex: 1; margin: 0 10px; }
                    .stat-number { font-size: 36px; font-weight: bold; margin: 10px 0; }
                    .rupture { color: #d32f2f; }
                    .critique { color: #f57c00; }
                    table { width: 100%%; border-collapse: collapse; margin: 20px 0; background: white; border-radius: 8px; }
                    th { background-color: #667eea; color: white; padding: 15px; text-align: left; }
                    td { padding: 12px; border-bottom: 1px solid #ddd; }
                    .footer { margin-top: 30px; text-align: center; color: #777; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>📊 Rapport Quotidien des Alertes de Stock</h1>
                        <p>%s</p>
                    </div>
                    
                    <div class="summary-box">
                        <div class="stat-card">
                            <div>Total des alertes</div>
                            <div class="stat-number">%d</div>
                        </div>
                        <div class="stat-card">
                            <div>🔴 Ruptures de stock</div>
                            <div class="stat-number rupture">%d</div>
                        </div>
                        <div class="stat-card">
                            <div>🟠 Stocks critiques</div>
                            <div class="stat-number critique">%d</div>
                        </div>
                    </div>
                    
                    <table>
                        <thead>
                            <tr>
                                <th>Stock</th>
                                <th>Produit ID</th>
                                <th>Quantité</th>
                                <th>Seuil</th>
                                <th>Statut</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    
                    <div class="footer">
                        <p>Email automatique généré par le système de gestion de stock</p>
                        <p style="margin-top: 10px; font-size: 10px;">
                            Batch exécuté quotidiennement à 23h35
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """,
                java.time.LocalDate.now(),
                alertes.size(),
                ruptureCount,
                critiqueCount,
                tableRows.toString()
        );
    }
}