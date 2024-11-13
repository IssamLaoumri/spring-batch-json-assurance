package com.laoumri.miniprojetspringbatch.services.impls;

import com.laoumri.miniprojetspringbatch.entities.MedicineReference;
import com.laoumri.miniprojetspringbatch.entities.Traitement;
import com.laoumri.miniprojetspringbatch.repositories.MedicineReferenceRepository;
import com.laoumri.miniprojetspringbatch.services.RemboursementStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraitementRemboursementStrategy implements RemboursementStrategy<Traitement> {
    private final MedicineReferenceRepository referenceRepository;
    @Override
    public double calculate(Traitement item) {
        MedicineReference reference = referenceRepository.findByName(item.getNomMedicament());
        return item.getPrixMedicament() * reference.getRefundRate();
    }
}
