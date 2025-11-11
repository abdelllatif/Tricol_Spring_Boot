package com.tricol.Tricol.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
        import lombok.Data;

@Entity
@Data
@Table(name = "stock_cump")
public class StockCUMP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonBackReference  // ← EMPÊCHE LA BOUCLE INFINIE
    private Produit produit;


    @Column(name = "cout_unitaire_cump", nullable = false)
    private Double coutUnitaireCUMP;
}
