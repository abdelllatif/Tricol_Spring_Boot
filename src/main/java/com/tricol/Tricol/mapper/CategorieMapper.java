package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.model.Categorie;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategorieMapper {
    CategorieDTO toDTO(Categorie entity);
    Categorie toEntity(CategorieDTO dto);
    @AfterMapping
    default void addProduitCount(@MappingTarget CategorieDTO dto, Categorie entity) {
        dto.setNombreProduits(entity.getProduits() != null ? entity.getProduits().size() : 0);
    }
}