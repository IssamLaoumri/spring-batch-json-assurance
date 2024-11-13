package com.laoumri.miniprojetspringbatch.configurations;

import com.laoumri.miniprojetspringbatch.configurations.processors.*;
import com.laoumri.miniprojetspringbatch.configurations.writers.DossierWriter;
import com.laoumri.miniprojetspringbatch.configurations.writers.ReferenceWriter;
import com.laoumri.miniprojetspringbatch.dto.ReferenceDTO;
import com.laoumri.miniprojetspringbatch.entities.Dossier;
import com.laoumri.miniprojetspringbatch.entities.Reference;
import com.laoumri.miniprojetspringbatch.entities.Traitement;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {
    private final DossierWriter dossierWriter;
    private final ReferenceWriter referenceWriter;

    /* ================== Les processeurs ==================== */
    private final ValidationProcessor validationProcessor;
    private final ConsultationProcessor consultationProcessor;
    private final TotalRemboursementProcessor totalRemboursementProcessor;
    private final TraitementMappingProcessor traitementMappingProcessor;
    private final TraitementRemboursementProcessor traitementRemboursementProcessor;
    private final ReferenceMappingProcessor referenceMappingProcessor;

    // Definition de Job
    @Bean
    public Job recordsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
//        Flow traitementDossiersJSONFlow = new FlowBuilder<SimpleFlow>("traitementDossiersJSONFlow")
//                .start(recordsStep(jobRepository, transactionManager))
//                .build();
//
//        Flow traitementDossiersXMLFlow = new FlowBuilder<SimpleFlow>("traitementDossiersXMLFlow")
//                .start(recordsXmlStep(jobRepository, transactionManager))
//                .build();
//
//        Flow parallelFlow = new FlowBuilder<SimpleFlow>("parallelFlow")
//                .start(traitementDossiersJSONFlow)
//                .split(new SimpleAsyncTaskExecutor())
//                .add(traitementDossiersXMLFlow)
//                .build();

        return new JobBuilder("job-data-load", jobRepository)
                .start(referenceStep(jobRepository, transactionManager))
                .next(recordsStep(jobRepository, transactionManager))
                .next(recordsXmlStep(jobRepository, transactionManager))
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

    @Bean
    public Step recordsXmlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws ItemStreamException {
        return new StepBuilder("step-xml-data-load", jobRepository)
                .<Dossier, Dossier>chunk(10, transactionManager)
                .reader(xmlDossierItemReader())
                .processor(dossierProcessor())
                .writer(dossierWriter)
                .allowStartIfComplete(true)
                //.faultTolerant()
                //.skip(Exception.class)
                //.skipLimit(3)
                //.listener(new BadRecordsListener())
                .build();
    }

    // Definition du Step pour l'enregistrement des reférences médicales
    @Bean
    public Step referenceStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws ItemStreamException {
        return new StepBuilder("reference-step-load", jobRepository)
                .<ReferenceDTO, Reference>chunk(100, transactionManager)
                .reader(referencesReader())
                .processor(referenceMappingProcessor)
                .writer(referenceWriter)
                .allowStartIfComplete(true)
                .build();
    }

    // Definition de Reader pour le fichier des references CSV
    @Bean
    public ItemReader<ReferenceDTO> referencesReader() {
        FlatFileItemReader<ReferenceDTO> reader = new FlatFileItemReader<>();
        reader.setName("REFERENCE-CSV-READER");
        reader.setResource(new ClassPathResource("references.csv"));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public StaxEventItemReader<Dossier> xmlDossierItemReader() {
        return new StaxEventItemReaderBuilder<Dossier>()
                .name("xmlDossierReader")
                .resource(new ClassPathResource("dossiers.xml"))
                .addFragmentRootElements("Dossier")
                .unmarshaller(dossierMarshaller())
                .build();

    }

//    @Bean
//    public Jaxb2Marshaller dossierMarshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setClassesToBeBound(Dossier.class, Traitement.class);
//        return marshaller;
//    }


    @Bean
    public XStreamMarshaller dossierMarshaller() {
        Map<String, String> aliases = new HashMap<>();

        aliases.put("Dossier", "com.laoumri.miniprojetspringbatch.entities.Dossier");
        aliases.put("Traitement", "com.laoumri.miniprojetspringbatch.entities.Traitement");

        XStreamMarshaller marshaller = new XStreamMarshaller();

        marshaller.setAliases(aliases);
        marshaller.setTypePermissions(AnyTypePermission.ANY);

        return marshaller;
    }

    // Line mapper pour CSV
    @Bean
    public LineMapper<ReferenceDTO> lineMapper() {
        DefaultLineMapper<ReferenceDTO> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("CODE","NOM","DCI1","DOSAGE1", "UNITE_DOSAGE1", "FORME", "PRESENTATION", "PPV", "PH", "PRIX_BR", "PRINCEPS_GENERIQUE", "TAUX_REMBOURSEMENT");
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<ReferenceDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ReferenceDTO.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
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
        processor.setFilter(true);
        return processor;
    }

    // Composition des processeurs dans l'ordre
    @Bean
    public CompositeItemProcessor<Dossier, Dossier> dossierProcessor() {
        CompositeItemProcessor<Dossier, Dossier> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(
                validatingItemProcessor(),
                calculProcessor()
        ));
        return processor;
    }

    @Bean
    public CompositeItemProcessor<Dossier, Dossier> calculProcessor(){
        CompositeItemProcessor<Dossier, Dossier> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(
                consultationProcessor,
                traitementMappingProcessor,
                traitementRemboursementProcessor,
                totalRemboursementProcessor
        ));
        return processor;
    }
}
