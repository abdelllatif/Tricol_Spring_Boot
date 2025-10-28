package com.tricol.Tricol.service;

import com.tricol.Tricol.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FournisseurService {
    Fournisseur save(Fournisseur fournisseur);
    Optional<Fournisseur> findById(Long id);
    List<Fournisseur> findAll();
    Page<Fournisseur> findAllPaged(Pageable pageable) ;
    void delete(Long id);
}
