package com.tricol.Tricol.repository;

import com.tricol.Tricol.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    // FournisseurRepository.java
    Page<Fournisseur> findBySocieteContainingIgnoreCaseOrIceContainingOrVilleContainingIgnoreCase(
            String societe, String ice, String ville, Pageable pageable);
}


