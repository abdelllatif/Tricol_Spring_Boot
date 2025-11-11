package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FournisseurService {

    Optional<Fournisseur> findById(Long id);

    List<Fournisseur> findAll();

    Page<Fournisseur> findAllPaged(Pageable pageable, String search);

    void delete(Long id);

    Fournisseur create(FournisseurDTO dto);

    Fournisseur update(Long id, FournisseurDTO dto);
}
