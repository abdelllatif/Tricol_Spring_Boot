package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.enums.TypeMouvement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProduitService {

    Optional<ProduitDTO> findById(Long id);
    List<ProduitDTO> findAll();
    Page<ProduitDTO> findAllPaged(String nom, Pageable pageable);  // ← NOUVEAU
    ProduitDTO create(ProduitDTO dto,Double prixAchat);
    ProduitDTO update(Long id, ProduitDTO dto);
    void delete(Long id);
    ProduitDTO ajusterStock(Long produitId, int quantite, double prixAchat, TypeMouvement type);
}
