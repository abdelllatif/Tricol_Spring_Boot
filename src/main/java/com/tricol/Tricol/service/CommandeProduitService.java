package com.tricol.Tricol.service;

import com.tricol.Tricol.model.CommandeProduit;

import java.util.List;
import java.util.Optional;

public interface CommandeProduitService {
    List<CommandeProduit> getAllCommandeProduits();
    Optional<CommandeProduit> getCommandeProduitById(Long id);
    CommandeProduit createCommandeProduit(CommandeProduit commandeProduit);
    CommandeProduit updateCommandeProduit(Long id, CommandeProduit commandeProduit);
    void deleteCommandeProduit(Long id);
}
