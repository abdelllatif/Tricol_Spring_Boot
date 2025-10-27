package com.tricol.Tricol.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "fournisseurs")
@Data
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String societe;

    @NotNull
    private String adresse;

    @NotNull
    private String contact;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String telephone;

    private String ville;

    private String ice;

}
