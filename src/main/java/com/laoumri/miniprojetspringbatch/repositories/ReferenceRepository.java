package com.laoumri.miniprojetspringbatch.repositories;

import com.laoumri.miniprojetspringbatch.entities.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
}
