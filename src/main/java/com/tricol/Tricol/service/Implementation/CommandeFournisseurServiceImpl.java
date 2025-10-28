package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.model.CommandeFournisseur;
import com.tricol.Tricol.repository.CommandeFournisseurRepository;
import com.tricol.Tricol.service.CommandeFournisseurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository repository;

    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CommandeFournisseur> getAllCommandes() {
        return repository.findAll();
    }

    @Override
    public Optional<CommandeFournisseur> getCommandeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CommandeFournisseur createCommande(CommandeFournisseur commande) {
        return repository.save(commande);
    }

    @Override
    public CommandeFournisseur updateCommande(Long id, CommandeFournisseur commande) {
        return repository.findById(id).map(existing -> {
            existing.setFournisseur(commande.getFournisseur());
            existing.setProduits(commande.getProduits());
            existing.setMontantTotal(commande.getMontantTotal());
            existing.setStatut(commande.getStatut());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("CommandeFournisseur not found with id " + id));
    }

    @Override
    public void deleteCommande(Long id) {
        repository.deleteById(id);
    }
}
