package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
