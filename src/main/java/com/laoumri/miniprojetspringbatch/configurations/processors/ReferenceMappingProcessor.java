package com.laoumri.miniprojetspringbatch.configurations.processors;

import com.laoumri.miniprojetspringbatch.dto.ReferenceDTO;
import com.laoumri.miniprojetspringbatch.entities.Reference;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMappingProcessor implements ItemProcessor<ReferenceDTO, Reference> {
    @Override
    public Reference process(ReferenceDTO item) {

        Reference newReference = Reference.builder()
                .CODE(Long.parseLong(item.getCODE()))
                .DCI1(item.getDCI1())
                .NOM(item.getNOM())
                .DOSAGE1(item.getDOSAGE1())
                .FORME(item.getFORME())
                .PH(Double.parseDouble(item.getPH()))
                .PPV(Double.parseDouble(item.getPPV()))
                .PRESENTATION(item.getPRESENTATION())
                .PRINCEPS_GENERIQUE(item.getPRINCEPS_GENERIQUE())
                .PRIX_BR(Double.parseDouble(item.getPRIX_BR()))
                .TAUX_REMBOURSEMENT(Double.parseDouble(item.getTAUX_REMBOURSEMENT()))
                .UNITE_DOSAGE1(item.getUNITE_DOSAGE1())
                .build();
        return newReference;
    }
}
