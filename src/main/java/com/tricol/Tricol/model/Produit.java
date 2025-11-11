package com.tricol.Tricol.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int stockActuel;

    // Link to CommandeProduit
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CommandeProduit> commandeProduits = new ArrayList<>();

    // Link to MouvementStock (1 product -> many movements)
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)

    private List<MouvementStock> mouvements = new ArrayList<>();

    // Link to StockCUMP (1 product -> 1 CUMP)
    @OneToOne(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JsonManagedReference
    @JsonProperty("stockCUMP")  // Force Jackson à l'inclure
    private StockCUMP stockCUMP;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
    }
