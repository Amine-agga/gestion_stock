package com.example.ms_stock.batch.config;

import com.example.ms_stock.batch.model.StockAlerte;
import com.example.ms_stock.batch.processor.StockCritiqueProcessor;
import com.example.ms_stock.batch.reader.StockItemsReader;
import com.example.ms_stock.batch.writer.StockAlerteWriter;
import com.example.ms_stock.model.StockItems;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StockBatchConfig {

    @Autowired
    private StockItemsReader stockItemsReader;

    @Autowired
    private StockCritiqueProcessor stockCritiqueProcessor;

    @Autowired
    private StockAlerteWriter stockAlerteWriter;
    @Bean
    public Step verifierStockCritiqueStep(JobRepository jobRepository,
                                          PlatformTransactionManager transactionManager) {
        return new StepBuilder("verifierStockCritiqueStep", jobRepository)
                .<StockItems, StockAlerte>chunk(10, transactionManager)
                .reader(stockItemsReader)
                .processor(stockCritiqueProcessor)
                .writer(stockAlerteWriter)
                .build();
    }
    @Bean
    public Job verificationStockCritiqueJob(JobRepository jobRepository,
                                            Step verifierStockCritiqueStep) {
        return new JobBuilder("verificationStockCritiqueJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(verifierStockCritiqueStep)
                .build();
    }
}