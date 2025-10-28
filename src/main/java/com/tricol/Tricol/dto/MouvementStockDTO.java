package com.tricol.Tricol.dto;

import com.tricol.Tricol.enums.TypeMouvement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MouvementStockDTO {

    @NotNull(message = "L'identifiant du produit est obligatoire.")
    private Long produitId;

    private Long commandeId; // optional, peut être null si mouvement non lié à une commande

    @NotNull(message = "Le type de mouvement est obligatoire.")
    private TypeMouvement typeMouvement;

    @Min(value = 1, message = "La quantité doit être au moins 1.")
    private int quantite;

    @Min(value = 0, message = "Le coût unitaire doit être supérieur ou égal à 0.")
    private Double coutUnitaire;

    @Size(max = 500, message = "La remarque ne doit pas dépasser 500 caractères.")
    private String remarque;
}
