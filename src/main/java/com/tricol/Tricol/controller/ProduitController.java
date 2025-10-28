package com.tricol.Tricol.controller;

import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.service.ProduitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        Produit savedProduit = produitService.save(produit);
        return ResponseEntity.ok(savedProduit);
    }

    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitService.findById(id);
        return produit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        Optional<Produit> existingProduit = produitService.findById(id);

        if (existingProduit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produit.setId(id);
        Produit updatedProduit = produitService.save(produit);
        return ResponseEntity.ok(updatedProduit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
