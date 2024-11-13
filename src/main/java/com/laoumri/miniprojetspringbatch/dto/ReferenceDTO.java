package com.laoumri.miniprojetspringbatch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReferenceDTO {
    private String CODE;
    private String NOM;
    private String DCI1;
    private String DOSAGE1;
    private String UNITE_DOSAGE1;
    private String FORME;
    private String PRESENTATION;
    private String PPV;
    private String PH;
    private String PRIX_BR;
    private char PRINCEPS_GENERIQUE;
    private String TAUX_REMBOURSEMENT;
}
