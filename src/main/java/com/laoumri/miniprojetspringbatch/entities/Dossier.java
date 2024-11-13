package com.laoumri.miniprojetspringbatch.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Dossier {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nomAssure;
    @Transient
    private String numeroAffiliation;
    @Transient
    private double prixConsultation;
    @Transient
    private int nombrePiecesJointes;
    @Transient
    private double remboursementConsultation;

    private String immatriculation;
    private String lienParente;
    private double montantTotalFrais;
    private String nomBeneficiaire;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dateDepotDossier;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Traitement> traitements;

    // Autres propriété
    private double totalRemboursement;
}
