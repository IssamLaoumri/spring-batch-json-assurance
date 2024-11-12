package com.laoumri.miniprojetspringbatch.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "medicine_reference")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineReference {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private double price;
    private double RefundRate;
}
