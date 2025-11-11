package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.MouvementStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    // List version
    List<MouvementStock> findByProduitId(Long produitId);
    // add this method
    Page<MouvementStock> findByProduitNomContainingAndDateMouvement(String nom, LocalDate dateMouvement, Pageable pageable);

    // Paged version
    Page<MouvementStock> findByProduitId(Long produitId, Pageable pageable);

    // Search by product name
    Page<MouvementStock> findByProduitNomContaining(String nom, Pageable pageable);
}
