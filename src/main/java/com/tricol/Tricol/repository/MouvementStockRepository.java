package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.MouvementStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
}
