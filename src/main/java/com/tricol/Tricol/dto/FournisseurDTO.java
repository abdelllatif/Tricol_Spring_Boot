package com.tricol.Tricol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FournisseurDTO {

    @NotBlank(message = "Le nom de la société est obligatoire.")
    @Size(max = 255, message = "Le nom de la société ne doit pas dépasser 255 caractères.")
    private String societe;

    @NotBlank(message = "L'adresse est obligatoire.")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères.")
    private String adresse;

    @NotBlank(message = "Le nom du contact est obligatoire.")
    @Size(max = 255, message = "Le nom du contact ne doit pas dépasser 255 caractères.")
    private String contact;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "L'email doit être valide.")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire.")
    @Pattern(regexp = "^[0-9+\\-() ]{6,20}$", message = "Le numéro de téléphone n'est pas valide.")
    private String telephone;

    @Size(max = 100, message = "Le nom de la ville ne doit pas dépasser 100 caractères.")
    private String ville;

    @Size(max = 50, message = "Le code ICE ne doit pas dépasser 50 caractères.")
    private String ice;
}
