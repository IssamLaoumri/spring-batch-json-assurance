package com.laoumri.miniprojetspringbatch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laoumri.miniprojetspringbatch.entities.Traitement;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DossierDTO {

    private String nomAssure;
    private String numeroAffiliation;
    private String immatriculation;
    private String lienParente;
    private double montantTotalFrais;
    private double prixConsultation;
    private int nombrePiecesJointes;
    private String nomBeneficiaire;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateDepotDossier;

    private List<Traitement> traitements;

    // Autres propriété
    private double remboursementConsultation;
    private double totalRemboursement;
}
