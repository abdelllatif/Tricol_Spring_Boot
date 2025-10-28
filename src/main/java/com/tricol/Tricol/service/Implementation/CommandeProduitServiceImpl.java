package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.model.CommandeProduit;
import com.tricol.Tricol.repository.CommandeProduitRepository;
import com.tricol.Tricol.service.CommandeProduitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeProduitServiceImpl implements CommandeProduitService {

    private final CommandeProduitRepository repository;

    public CommandeProduitServiceImpl(CommandeProduitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CommandeProduit> getAllCommandeProduits() {
        return repository.findAll();
    }

    @Override
    public Optional<CommandeProduit> getCommandeProduitById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CommandeProduit createCommandeProduit(CommandeProduit commandeProduit) {
        return repository.save(commandeProduit);
    }

    @Override
    public CommandeProduit updateCommandeProduit(Long id, CommandeProduit commandeProduit) {
        return repository.findById(id).map(existing -> {
            existing.setCommande(commandeProduit.getCommande());
            existing.setProduit(commandeProduit.getProduit());
            existing.setQuantite(commandeProduit.getQuantite());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("CommandeProduit not found with id " + id));
    }

    @Override
    public void deleteCommandeProduit(Long id) {
        repository.deleteById(id);
    }
}
