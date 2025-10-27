package com.tricol.Tricol.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commande_produit")
public class CommandeProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private CommandeFournisseur commande;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    private int quantite;
}
