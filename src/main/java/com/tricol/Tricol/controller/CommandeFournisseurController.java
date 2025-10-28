package com.tricol.Tricol.controller;

import com.tricol.Tricol.model.CommandeFournisseur;
import com.tricol.Tricol.service.CommandeFournisseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes-fournisseur")
@CrossOrigin(origins = "*")
public class CommandeFournisseurController {

    private final CommandeFournisseurService service;

    public CommandeFournisseurController(CommandeFournisseurService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommandeFournisseur> getAllCommandes() {
        return service.getAllCommandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeFournisseur> getCommandeById(@PathVariable Long id) {
        return service.getCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CommandeFournisseur createCommande(@RequestBody CommandeFournisseur commande) {
        return service.createCommande(commande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeFournisseur> updateCommande(@PathVariable Long id,
                                                              @RequestBody CommandeFournisseur commande) {
        try {
            return ResponseEntity.ok(service.updateCommande(id, commande));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        service.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
}
