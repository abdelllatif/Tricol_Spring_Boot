package com.tricol.Tricol.repository;

import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.model.MouvementStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    List<MouvementStock> findByProduitId(Long produitId);
    Page<MouvementStock> findByProduitNomContainingAndDateMouvement(String nom, LocalDate dateMouvement, Pageable pageable);

    Page<MouvementStock> findByProduitId(Long produitId, Pageable pageable);

    Page<MouvementStock> findByProduitNomContaining(String nom, Pageable pageable);

    List<MouvementStock> findByProduitIdAndTypeMouvement(Long produitId, TypeMouvement typeMouvement);

}
