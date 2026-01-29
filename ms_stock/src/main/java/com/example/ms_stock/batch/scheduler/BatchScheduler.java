package com.example.ms_stock.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BatchScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job verificationStockCritiqueJob;
    @Scheduled(cron = "0 15 11 * * ?")
    public void executerVerificationStockQuotidienne() {
        logger.info("🚀 Démarrage du batch de vérification des stocks critiques - " + LocalDateTime.now());
        try {
            // Créer des paramètres uniques pour chaque exécution
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("executionDate", LocalDateTime.now().toString())
                    .toJobParameters();
            // Lancer le job
            jobLauncher.run(verificationStockCritiqueJob, jobParameters);
            logger.info("✅ Batch de vérification des stocks terminé avec succès");
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'exécution du batch de vérification des stocks", e);
        }
    }
    public void executerManuellement() {
        logger.info("🔧 Exécution manuelle du batch de vérification des stocks");
        executerVerificationStockQuotidienne();
    }
}