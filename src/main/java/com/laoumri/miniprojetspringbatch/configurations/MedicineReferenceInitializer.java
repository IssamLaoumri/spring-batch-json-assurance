package com.laoumri.miniprojetspringbatch.configurations;

import com.laoumri.miniprojetspringbatch.entities.MedicineReference;
import com.laoumri.miniprojetspringbatch.repositories.MedicineReferenceRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class MedicineReferenceInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(MedicineReferenceRepository repository) {
        return args -> {
            try {
                List<MedicineReference> medicines = Arrays.asList(
                        new MedicineReference(null, "Paracétamol", 5.0, 0.7),
                        //new MedicineReference(null, "Ibuprofène", 8.0, 0.6),
                        new MedicineReference(null, "Amoxicilline", 12.0, 0.8),
                        new MedicineReference(null, "Aspirine", 4.0, 0.65),
                        new MedicineReference(null, "Doliprane", 3.5, 0.75),
                        new MedicineReference(null, "Diazepam", 10.0, 0.75),
                        new MedicineReference(null, "Metformine", 15.0, 0.85),
                        new MedicineReference(null, "Oméprazole", 9.0, 0.75)
                );
                repository.saveAll(medicines);
                log.info("{} medicines have been initialized in the database.", medicines.size());
            } catch (Exception e) {
                log.error("An error occurred while initializing medicines in the database.", e);
            }
        };
    }
}

