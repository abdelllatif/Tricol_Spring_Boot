package com.tricol.Tricol.dto;

import com.tricol.Tricol.enums.StatutCommande;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeFournisseurDTO {
    private Long id;

    @NotNull(message = "L'identifiant du fournisseur est obligatoire.")
    private Long fournisseurId;
    private FournisseurDTO fournisseur;
    @Valid
    @NotNull(message = "La liste des produits est obligatoire.")
    private List<CommandeProduitDTO> produits;

    @Min(value = 0, message = "Le montant total doit être supérieur ou égal à 0.")
    private double montantTotal;

    @NotNull(message = "Le statut de la commande est obligatoire.")
    private StatutCommande statut;

    private LocalDateTime dateCommande; // correspond à date_commande
    private LocalDateTime createdAt;    // correspond à created_at
    private LocalDateTime updatedAt;    // correspond à updated_at
}
