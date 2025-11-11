package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.dto.StockCUMPDTO;
import com.tricol.Tricol.model.Produit;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    Produit toEntity(ProduitDTO dto);
    ProduitDTO toDTO(Produit produit);

    @AfterMapping
    default void linkStockCUMP(@MappingTarget ProduitDTO dto, Produit entity) {
        if (entity.getStockCUMP() != null) {
            dto.setStockCUMP(new StockCUMPDTO(
                    entity.getStockCUMP().getId(),
                    entity.getStockCUMP().getCoutUnitaireCUMP()
            ));
        } else {
            // Si pas de CUMP (anciens produits), on le crée dans le DTO
            dto.setStockCUMP(new StockCUMPDTO(null, entity.getPrixUnitaire()));
        }
    }

    @AfterMapping
    default void mapCategorie(@MappingTarget ProduitDTO dto, Produit entity) {
        if (entity.getCategorie() != null) {
            dto.setCategorie(new CategorieDTO());
            dto.getCategorie().setId(entity.getCategorie().getId());
            dto.getCategorie().setNom(entity.getCategorie().getNom());
            dto.getCategorie().setNombreProduits(entity.getCategorie().getProduits().size());
        }
    }
}
