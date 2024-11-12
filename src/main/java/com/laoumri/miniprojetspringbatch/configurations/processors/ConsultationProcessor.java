package com.laoumri.miniprojetspringbatch.configurations.processors;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsultationProcessor implements ItemProcessor<Dossier, Dossier> {
    @Value("${param.CONSULTATION_REFUND_RATE}")
    private double CONSULTATION_REFUND_RATE;

    @Override
    public Dossier process(Dossier item) {
        // Calcul du remboursement de la consultation
        double remboursementConsultation = item.getPrixConsultation() * CONSULTATION_REFUND_RATE;
        // Ajout du montant de remboursement de consultation
        item.setRemboursementConsultation(remboursementConsultation);
        log.info(item.toString());
        return item;
    }
}
