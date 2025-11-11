package com.tricol.Tricol.model;

import com.tricol.Tricol.enums.TypeMouvement;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "mouvement_stock")
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec Produit
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mouvement_produit"))
    private Produit produit;

    // Relation avec CommandeFournisseur
    @ManyToOne
    @JoinColumn(name = "commande_id",
            foreignKey = @ForeignKey(name = "fk_mouvement_commande"))
    private CommandeFournisseur commande;

    // Enum pour type_mouvement
    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement", nullable = false, length = 20)
    private TypeMouvement typeMouvement;

    @Column(nullable = false)
    private int quantite;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement = LocalDateTime.now();

    @Column(name = "cout_unitaire")
    private Double coutUnitaire;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}