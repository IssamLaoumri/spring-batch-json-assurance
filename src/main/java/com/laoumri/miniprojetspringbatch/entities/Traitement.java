package com.laoumri.miniprojetspringbatch.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Traitement {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String codeBarre;
    private String nomMedicament;
    private String typeMedicament;
    private double prixMedicament;
    private boolean existe;
    // ici le montant remboursable
    private double montantRemboursable;
}
