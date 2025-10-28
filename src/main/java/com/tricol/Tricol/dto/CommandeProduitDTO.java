package com.tricol.Tricol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommandeProduitDTO {
    @NotNull(message = "L'identifiant du Commande de Fournisseur est obligatoire.")
    private Long CommandeFournisseur;

    @NotNull(message = "L'identifiant du produit est obligatoire.")
    private Long produitId;

    @Min(value = 1, message = "La quantité doit être d'au moins 1.")
    private int quantite;
}
