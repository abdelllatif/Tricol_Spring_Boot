package com.tricol.Tricol.mapper;

import com.tricol.Tricol.dto.MouvementStockDTO;
import com.tricol.Tricol.model.MouvementStock;

public class MouvementStockMapper {

    public static MouvementStockDTO toDTO(MouvementStock ms) {
        if (ms == null) return null;

        MouvementStockDTO dto = new MouvementStockDTO();
        dto.setProduitId(ms.getProduit() != null ? ms.getProduit().getId() : null);
        dto.setTypeMouvement(ms.getTypeMouvement());
        dto.setQuantite(ms.getQuantite());
        dto.setCoutUnitaire(ms.getCoutUnitaire());
        dto.setCommandeId(ms.getCommande() != null ? ms.getCommande().getId() : null);
        dto.setDateMouvement(ms.getDateMouvement());
        return dto;
    }

    public static MouvementStock toEntity(MouvementStockDTO dto, MouvementStock entity) {
        if (dto == null) return null;
        if (entity == null) entity = new MouvementStock();

        entity.setTypeMouvement(dto.getTypeMouvement());
        entity.setQuantite(dto.getQuantite());
        entity.setCoutUnitaire(dto.getCoutUnitaire());
        entity.setDateMouvement(dto.getDateMouvement());
        return entity;
    }
}
