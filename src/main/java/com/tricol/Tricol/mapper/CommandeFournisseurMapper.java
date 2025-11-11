package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import com.tricol.Tricol.dto.CommandeProduitDTO;
import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.model.CommandeFournisseur;
import com.tricol.Tricol.model.CommandeProduit;
import com.tricol.Tricol.model.Fournisseur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommandeFournisseurMapper {

    @Mapping(source = "fournisseur.id", target = "fournisseurId")
    @Mapping(source = "fournisseur", target = "fournisseur") // ✅ map the object
    @Mapping(source = "produits", target = "produits")
    @Mapping(source = "dateCommande", target = "dateCommande")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    CommandeFournisseurDTO toDTO(CommandeFournisseur entity);

    @Mapping(source = "produit.id", target = "produitId")
    CommandeProduitDTO toDTO(CommandeProduit entity);

    FournisseurDTO toDTO(Fournisseur fournisseur); // ✅ new method

    List<CommandeProduitDTO> toDTO(List<CommandeProduit> entities);
}
