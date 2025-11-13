package com.tricol.Tricol.controller;

import com.tricol.Tricol.dto.MouvementStockDTO;
import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }
    @Operation(summary = "Create a new product", description = "Create a new product")
    @PostMapping
    public ResponseEntity<ProduitDTO> createProduit(@Valid @RequestBody ProduitDTO produitDTO, Double prixAchat) {
        ProduitDTO created = produitService.create(produitDTO,prixAchat);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        List<ProduitDTO> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }
    @Operation(summary = "Get all products with pagination support", description = "Retrieve products with pagination support")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllPaged(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<ProduitDTO> page = produitService.findAllPaged(search,pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Update product", description = "Update an existing product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO updated = produitService.update(id, produitDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Delete product", description = "Delete a product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ajouter un stock au produit", description = "Ajouter un stock au produit")
    @PutMapping("/{id}/ajuster-stock")
    public ResponseEntity<ProduitDTO> ajusterStock(
            @PathVariable Long id,
            @Valid @RequestBody MouvementStockDTO dto) {
        try {
            ProduitDTO updated = produitService.ajusterStock(
                    id,
                    dto.getQuantite(),
                    dto.getCoutUnitaire(),
                    dto.getTypeMouvement()
            );
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
