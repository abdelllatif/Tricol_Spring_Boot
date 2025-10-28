package com.tricol.Tricol.dto;


import com.tricol.Tricol.enums.StatutCommande;
import com.tricol.Tricol.model.CommandeProduit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CommandeFournisseurDTO {
    @NotNull(message = "L'identifiant du fournisseur est obligatoire.")
    private Long fournisseurId;

    @Valid
    @NotNull(message = "La liste des produits est obligatoire.")
    private List<CommandeProduit> produits;

    @Min(value = 0, message = "Le montant total doit être supérieur ou égal à 0.")
    private double montantTotal;

    @NotNull(message = "Le statut de la commande est obligatoire.")
    private StatutCommande statut;
}
