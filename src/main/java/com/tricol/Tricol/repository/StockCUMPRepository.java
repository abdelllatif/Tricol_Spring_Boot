package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.StockCUMP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockCUMPRepository extends JpaRepository<StockCUMP, Long> {
    Optional<StockCUMP> findByProduitId(Long produitId);
}
