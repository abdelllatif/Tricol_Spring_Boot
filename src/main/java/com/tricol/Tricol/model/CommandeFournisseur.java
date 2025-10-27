package com.tricol.Tricol.model;

import com.tricol.Tricol.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "commandes_fournisseur")
public class CommandeFournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeProduit> produits = new ArrayList<>();

    private double montantTotal;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut; // EN_ATTENTE, VALIDEE, LIVREE, ANNULEE
}
