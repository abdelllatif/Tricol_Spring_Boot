package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Page<Categorie> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}