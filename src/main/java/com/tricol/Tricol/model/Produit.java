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

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CommandeProduit> commandeProduits = new ArrayList<>();

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)

    private List<MouvementStock> mouvements = new ArrayList<>();

    @OneToOne(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JsonManagedReference
    @JsonProperty("stockCUMP")
    private StockCUMP stockCUMP;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
    }
