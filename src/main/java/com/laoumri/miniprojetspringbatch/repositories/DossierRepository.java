package com.laoumri.miniprojetspringbatch.repositories;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DossierRepository extends JpaRepository<Dossier, UUID> {
}
