package com.laoumri.miniprojetspringbatch.configurations;

import com.laoumri.miniprojetspringbatch.configurations.processors.*;
import com.laoumri.miniprojetspringbatch.configurations.writers.DossierWriter;
import com.laoumri.miniprojetspringbatch.entities.Dossier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final DossierWriter dossierWriter;

    /* ================== Les processeurs ==================== */
    private final ValidationProcessor validationProcessor;
    private final ConsultationProcessor consultationProcessor;
    private final TotalRemboursementProcessor totalRemboursementProcessor;
    private final TraitementMappingProcessor traitementMappingProcessor;
    private final TraitementRemboursementProcessor traitementRemboursementProcessor;


    // Definition de Job
    @Bean
    public Job recordsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-data-load", jobRepository)
                .start(recordsStep(jobRepository, transactionManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    // Definition du step
    @Bean
    public Step recordsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws ItemStreamException {
        return new StepBuilder("step-data-load", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(jsonItemReader())
                .processor(dossierProcessor())
                .writer(dossierWriter)
                .allowStartIfComplete(true)
                //.faultTolerant()
                //.skip(Exception.class)
                //.skipLimit(3)
                //.listener(new BadRecordsListener())
                .build();
    }

    // Lire chaque dossier a partir de fichier JSON
    @Bean
    public JsonItemReader<Dossier> jsonItemReader() {
        return new JsonItemReaderBuilder<Dossier>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Dossier.class))
                .resource(new ClassPathResource("dossiers.json"))
                .name("dossierJsonItemReader")
                .build();
    }

    // Validation des entrées
    @Bean
    public ValidatingItemProcessor<Dossier> validatingItemProcessor(){
        ValidatingItemProcessor<Dossier> processor = new ValidatingItemProcessor<>();
        processor.setValidator(validationProcessor);
        processor.setFilter(true); // Indicating that the item should be filtred (skippable)
        return processor;
    }

    // Composition des processeurs dans l'ordre
    @Bean
    public CompositeItemProcessor<Dossier, Dossier> dossierProcessor() {
        CompositeItemProcessor<Dossier, Dossier> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(
                validatingItemProcessor(),
                consultationProcessor,
                traitementMappingProcessor,
                traitementRemboursementProcessor,
                totalRemboursementProcessor
        ));
        return processor;
    }
}
