package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurService {

    List<CommandeFournisseurDTO> getAllCommandes();

    Optional<CommandeFournisseurDTO> getCommandeById(Long id);

    CommandeFournisseurDTO createCommande(CommandeFournisseurDTO dto);
    CommandeFournisseurDTO updateCommande(Long id, CommandeFournisseurDTO dto);
    Page<CommandeFournisseurDTO> findAllPaged(String search, String statut, Pageable pageable);
    void deleteCommande(Long id);
}
