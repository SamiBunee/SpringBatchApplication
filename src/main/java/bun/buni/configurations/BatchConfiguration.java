package bun.buni.configurations;

import bun.buni.batch.steps.ItemDecompressStep;
import bun.buni.batch.steps.ItemProcessorStep;
import bun.buni.batch.steps.ItemReaderStep;
import bun.buni.batch.steps.ItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobRepository jobRepository;

    @Autowired
    public PlatformTransactionManager platformTransactionManager;

    @Bean
    @JobScope
    public ItemDecompressStep itemDecompressStep() {
        return new ItemDecompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep() {
        return new ItemWriterStep();
    }

    /**
     * STEPS:
     */
    @Bean
    public Step decompressFileStep() {
        return new StepBuilder("itemDecompressStep", jobRepository)
                .tasklet(itemDecompressStep(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step readFileStep() {
        return new StepBuilder("itemReaderStep", jobRepository)
                .tasklet(itemReaderStep(),platformTransactionManager)
                .build();
    }

    @Bean
    public Step processDataStep() {
        return new StepBuilder("itemProcessorStep", jobRepository)
                .tasklet(itemProcessorStep(),platformTransactionManager)
                .build();
    }

    @Bean
    public Step writeDataStep() {
        return new StepBuilder("itemWriterStep", jobRepository)
                .tasklet(itemWriterStep(),platformTransactionManager)
                .build();
    }

    /*
     * JOBS:
     */
    @Bean
    public Job readCSVJob(){
        return new JobBuilder("readCSVJob", jobRepository)
                .start(decompressFileStep())
                .next(readFileStep())
                .next(processDataStep())
                .next(writeDataStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
