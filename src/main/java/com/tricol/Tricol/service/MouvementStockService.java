package com.tricol.Tricol.service;

import com.tricol.Tricol.model.MouvementStock;

import java.util.List;
import java.util.Optional;

public interface MouvementStockService {
    List<MouvementStock> getAllMouvements();
    Optional<MouvementStock> getMouvementById(Long id);
    MouvementStock createMouvement(MouvementStock mouvementStock);
    MouvementStock updateMouvement(Long id, MouvementStock mouvementStock);
    void deleteMouvement(Long id);
}
