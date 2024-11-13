package com.laoumri.miniprojetspringbatch.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "references")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Reference {
    @Id
    private Long CODE;
    private String NOM;
    private String DCI1;
    private String DOSAGE1;
    private String UNITE_DOSAGE1;
    private String FORME;
    private String PRESENTATION;
    private double PPV;
    private double PH;
    private double PRIX_BR;
    private char PRINCEPS_GENERIQUE;
    private double TAUX_REMBOURSEMENT;
}
