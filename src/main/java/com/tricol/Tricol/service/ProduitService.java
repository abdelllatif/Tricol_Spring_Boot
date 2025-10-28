package com.tricol.Tricol.service;

import com.tricol.Tricol.model.Produit;
import java.util.List;
import java.util.Optional;

public interface ProduitService {
    Produit save(Produit produit);
    Optional<Produit> findById(Long id);
    List<Produit> findAll();
    void delete(Long id);
}
