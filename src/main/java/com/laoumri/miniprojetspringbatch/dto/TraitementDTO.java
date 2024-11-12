package com.laoumri.miniprojetspringbatch.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TraitementDTO {
    private String codeBarre;
    private String nomMedicament;
    private String typeMedicament;
    private double prixMedicament;
    private boolean existe;
    // ici le montant remboursable
    private double montantRemboursable;
}
