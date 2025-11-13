package com.tricol.Tricol.repository;

import com.tricol.Tricol.enums.StatutCommande;
import com.tricol.Tricol.model.CommandeFournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Long> {
    Page<CommandeFournisseur> findByStatut(StatutCommande statut, Pageable pageable);

    Page<CommandeFournisseur> findByFournisseurSocieteContainingIgnoreCase(String societe, Pageable pageable);

    Page<CommandeFournisseur> findByStatutAndFournisseurSocieteContainingIgnoreCase(
            StatutCommande statut,
            String societe,
            Pageable pageable
    );
}

