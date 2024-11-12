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
    private String numeroAffiliation;
    private String immatriculation;
    private String lienParente;
    private double montantTotalFrais;
    private double prixConsultation;
    private int nombrePiecesJointes;
    private String nomBeneficiaire;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateDepotDossier;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Traitement> traitements;

    // Autres propriété
    private double remboursementConsultation;
    private double totalRemboursement;
}
