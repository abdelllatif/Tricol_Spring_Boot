package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Page<Produit> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
