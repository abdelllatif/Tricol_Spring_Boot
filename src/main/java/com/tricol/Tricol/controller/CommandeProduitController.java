package com.tricol.Tricol.controller;

import com.tricol.Tricol.model.CommandeProduit;
import com.tricol.Tricol.service.CommandeProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commande-produits")
@CrossOrigin(origins = "*")
public class CommandeProduitController {

    private final CommandeProduitService service;

    public CommandeProduitController(CommandeProduitService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommandeProduit> getAllCommandeProduits() {
        return service.getAllCommandeProduits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeProduit> getCommandeProduitById(@PathVariable Long id) {
        return service.getCommandeProduitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CommandeProduit createCommandeProduit(@RequestBody CommandeProduit commandeProduit) {
        return service.createCommandeProduit(commandeProduit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeProduit> updateCommandeProduit(@PathVariable Long id,
                                                                 @RequestBody CommandeProduit commandeProduit) {
        try {
            return ResponseEntity.ok(service.updateCommandeProduit(id, commandeProduit));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommandeProduit(@PathVariable Long id) {
        service.deleteCommandeProduit(id);
        return ResponseEntity.noContent().build();
    }
}
