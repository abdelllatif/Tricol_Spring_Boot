package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.model.Produit;
import java.util.List;
import java.util.Optional;

public interface ProduitService {
    Optional<Produit> findById(Long id);
    List<Produit> findAll();
    void delete(Long id);
    Produit create(ProduitDTO dto);
    Produit update(Long id, ProduitDTO dto);
}
