package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.model.Fournisseur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {
    Fournisseur toEntity(FournisseurDTO dto);
    FournisseurDTO toDTO(Fournisseur fournisseur);
}
