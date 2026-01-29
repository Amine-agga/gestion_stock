package com.example.ms_stock.batch.writer;

import com.example.ms_stock.batch.model.StockAlerte;
import com.example.ms_stock.model.StockStatus;
import com.example.ms_stock.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockAlerteWriter implements ItemWriter<StockAlerte> {

    private static final Logger logger = LoggerFactory.getLogger(StockAlerteWriter.class);
    private static final String ALERT_DIR = "logs/stock-alerts";
    @Autowired
    private EmailService emailService;
    @Value("${stock.alert.email.send-individual:false}")
    private boolean sendIndividualEmails;
    @Value("${stock.alert.email.send-summary:true}")
    private boolean sendSummaryEmail;
    @Override
    public void write(Chunk<? extends StockAlerte> chunk) throws Exception {
        if (chunk.isEmpty()) {
            logger.info("✅ Aucune alerte de stock critique aujourd'hui");
            return;
        }
        logger.warn("🚨 " + chunk.size() + " alerte(s) de stock critique détectée(s)");

        // Créer le répertoire s'il n'existe pas
        Path alertDir = Paths.get(ALERT_DIR);
        if (!Files.exists(alertDir)) {
            Files.createDirectories(alertDir);
        }
        // Nom du fichier avec la date du jour
        String fileName = String.format("%s/stock-alerts-%s.txt",
                ALERT_DIR,
                LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        // Liste pour le récapitulatif email
        List<StockAlerte> alertesList = new ArrayList<>();
        // Écrire les alertes dans un fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("\n========================================\n");
            writer.write("RAPPORT D'ALERTES STOCK - " + LocalDate.now() + "\n");
            writer.write("========================================\n\n");

            for (StockAlerte alerte : chunk) {
                // Ajouter à la liste pour le récapitulatif
                alertesList.add(alerte);

                // Logger l'alerte
                logger.warn(alerte.getMessage());

                // Déterminer l'icône selon le statut
                String icone = alerte.getStockStatus() == StockStatus.EN_RUPTURE ? "🔴" : "🟠";

                // Écrire dans le fichier
                writer.write(String.format(
                        "%s ALERTE %s\n" +
                                "─────────────────────────────────────────\n" +
                                "Stock ID       : %d\n" +
                                "Stock          : %s\n" +
                                "Produit ID     : %d\n" +
                                "Quantité       : %d unités\n" +
                                "Seuil minimum  : %d unités\n" +
                                "Statut         : %s\n" +
                                "Message        : %s\n" +
                                "Date           : %s\n\n",
                        icone,
                        alerte.getStockStatus(),
                        alerte.getStockId(),
                        alerte.getStockNom(),
                        alerte.getIdProduit(),
                        alerte.getQuantite(),
                        alerte.getSeuilMinimum(),
                        alerte.getStockStatus(),
                        alerte.getMessage(),
                        alerte.getDateAlerte()
                ));

                //Envoyer un email pour chaque alerte (si activé)
                if (sendIndividualEmails) {
                    emailService.sendStockCritiqueAlert(alerte);
                }
            }

            writer.write("========================================\n");
            writer.write("Total des alertes: " + chunk.size() + "\n");
            writer.write("========================================\n");

            logger.info("📄 Rapport d'alertes sauvegardé: " + fileName);
        } catch (IOException e) {
            logger.error("❌ Erreur lors de l'écriture du rapport d'alertes", e);
            throw e;
        }

        //Envoyer un email récapitulatif (si activé)
        if (sendSummaryEmail && !alertesList.isEmpty()) {
            emailService.sendDailyAlertSummary(alertesList);
        }
    }
}