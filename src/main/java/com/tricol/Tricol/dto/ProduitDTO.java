package com.tricol.Tricol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProduitDTO {

    @NotBlank(message = "Le nom du produit est obligatoire.")
    @Size(max = 255, message = "Le nom du produit ne doit pas dépasser 255 caractères.")
    private String nom;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères.")
    private String description;

    @NotNull(message = "Le prix unitaire est obligatoire.")
    @Min(value = 0, message = "Le prix unitaire doit être supérieur ou égal à 0.")
    private Double prixUnitaire;


    @NotNull(message = "Le stock actuel est obligatoire.")
    @Min(value = 0, message = "Le stock actuel doit être supérieur ou égal à 0.")
    private Integer stockActuel;
}
