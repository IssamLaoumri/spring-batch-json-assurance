package com.laoumri.miniprojetspringbatch.repositories;

import com.laoumri.miniprojetspringbatch.entities.MedicineReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicineReferenceRepository extends JpaRepository<MedicineReference, UUID> {
    MedicineReference findByName(String name);
    boolean existsByName(String name);
}
