package com.tricol.Tricol.service;


import com.tricol.Tricol.model.CommandeFournisseur;
import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurService {
    List<CommandeFournisseur> getAllCommandes();
    Optional<CommandeFournisseur> getCommandeById(Long id);
    CommandeFournisseur createCommande(CommandeFournisseur commande);
    CommandeFournisseur updateCommande(Long id, CommandeFournisseur commande);
    void deleteCommande(Long id);
}
