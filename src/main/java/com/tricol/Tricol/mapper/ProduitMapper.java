package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.model.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    Produit toEntity(ProduitDTO dto);
    ProduitDTO toDTO(Produit produit);
}
