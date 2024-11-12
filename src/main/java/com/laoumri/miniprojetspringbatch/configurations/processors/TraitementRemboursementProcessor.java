package com.laoumri.miniprojetspringbatch.configurations.processors;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import com.laoumri.miniprojetspringbatch.entities.MedicineReference;
import com.laoumri.miniprojetspringbatch.entities.Traitement;
import com.laoumri.miniprojetspringbatch.repositories.MedicineReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TraitementRemboursementProcessor implements ItemProcessor<Dossier, Dossier> {
    private final MedicineReferenceRepository referenceRepository;

    @Override
    public Dossier process(Dossier item) throws Exception {
        List<Traitement> traitements = new ArrayList<>();
        for (Traitement traitement : item.getTraitements()) {
            MedicineReference reference = referenceRepository.findByName(traitement.getNomMedicament());
            double remboursementTraitement = traitement.getPrixMedicament() * reference.getRefundRate();
            traitement.setMontantRemboursable(remboursementTraitement);
            traitements.add(traitement);
        }
        item.setTraitements(traitements);
        log.info(item.toString());
        return item;
    }
}
