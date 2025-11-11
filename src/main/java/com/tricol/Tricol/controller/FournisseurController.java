package com.tricol.Tricol.controller;

import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.service.FournisseurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/fournisseurs")
@Tag(name = "Fournisseurs", description = "CRUD operations for suppliers")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers")
    @GetMapping
    public ResponseEntity<List<Fournisseur>> getAll() {
        return ResponseEntity.ok(fournisseurService.findAll());
    }

    @Operation(summary = "Get paginated suppliers", description = "Retrieve suppliers with pagination support")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllPaged(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<Fournisseur> page = fournisseurService.findAllPaged(pageable, search);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get supplier by ID", description = "Retrieve a supplier by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getById(@PathVariable Long id) {
        return fournisseurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new supplier", description = "Add a new supplier to the system")
    @PostMapping
    public ResponseEntity<Fournisseur> create(@Valid @RequestBody FournisseurDTO dto) {
        Fournisseur created = fournisseurService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update supplier", description = "Update an existing supplier by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> update(@PathVariable Long id, @Valid @RequestBody FournisseurDTO dto) {
        Fournisseur updated = fournisseurService.update(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete supplier", description = "Delete a supplier by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
