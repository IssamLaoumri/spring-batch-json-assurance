package com.laoumri.miniprojetspringbatch.services.impls;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import com.laoumri.miniprojetspringbatch.services.RemboursementStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsultationRemboursementStrategy implements RemboursementStrategy<Dossier> {
    @Value("${param.CONSULTATION_REFUND_RATE}")
    private double consultationRefundRate;

    @Override
    public double calculate(Dossier item) {
        return item.getPrixConsultation() * consultationRefundRate;
    }
}
