package com.tricol.Tricol.controller;

import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }
    @Operation(summary = "Create a new category", description = "Create a new category")
    @PostMapping
    public ResponseEntity<CategorieDTO> create(@Valid @RequestBody CategorieDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }
    @Operation(summary = "Get all categories with pagination support", description = "Retrieve categories with pagination support")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllPaged(
            Pageable pageable,
            @RequestParam(required = false) String search) {

        Page<CategorieDTO> page = service.findAllPaged(search, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @GetMapping
    public ResponseEntity<List<CategorieDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategorieDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @Operation(summary = "Update category", description = "Update an existing category by ID")
    @PutMapping("/{id}")
    public ResponseEntity<CategorieDTO> update(@PathVariable Long id, @Valid @RequestBody CategorieDTO dto) {
        CategorieDTO updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    @Operation(summary = "Delete category", description = "Delete a category by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}