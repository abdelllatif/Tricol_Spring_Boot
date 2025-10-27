package com.tricol.Tricol.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private double prixUnitaire;
    private String categorie;
    private int stockActuel;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<CommandeProduit> commandeProduits = new ArrayList<>();
}
