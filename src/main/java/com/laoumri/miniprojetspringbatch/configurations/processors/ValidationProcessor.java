package com.laoumri.miniprojetspringbatch.configurations.processors;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class ValidationProcessor implements Validator<Dossier> {
    @Override
    public void validate(Dossier item) {
        if(item.getNomAssure() == null || item.getNumeroAffiliation() == null)
            throw new ValidationException("Le nom de l’assuré et le numéro d’affiliation ne doivent pas être vides.");
        if (item.getPrixConsultation() <= 0 || item.getMontantTotalFrais() <= 0)
            throw new ValidationException("Les montants doivent être positifs.");
        if (item.getTraitements() == null || item.getTraitements().isEmpty())
            throw new ValidationException("La liste des traitements ne peut pas être vide.");
    }
}
