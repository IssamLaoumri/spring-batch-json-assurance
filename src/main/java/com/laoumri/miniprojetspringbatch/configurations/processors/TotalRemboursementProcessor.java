package com.laoumri.miniprojetspringbatch.configurations.processors;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import com.laoumri.miniprojetspringbatch.entities.Traitement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TotalRemboursementProcessor implements ItemProcessor<Dossier, Dossier> {
    @Override
    public Dossier process(Dossier item) {
        double totalRemboursementTraitements = 0.0;
        for(Traitement traitement : item.getTraitements()){
            totalRemboursementTraitements += traitement.getMontantRemboursable();
        }
        totalRemboursementTraitements += item.getRemboursementConsultation();
        item.setTotalRemboursement(totalRemboursementTraitements);
        log.info(item.toString());
        return item;
    }
}
