package com.tricol.Tricol.controller;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import com.tricol.Tricol.service.CommandeFournisseurService;
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
@RequestMapping("/commandes")
public class CommandeFournisseurController {

    private final CommandeFournisseurService service;

    public CommandeFournisseurController(CommandeFournisseurService service) {
        this.service = service;
    }
    @Operation(summary = "Get all commands", description = "Retrieve a list of all commands")
    @GetMapping
    public ResponseEntity<List<CommandeFournisseurDTO>> getAllCommandes() {
        List<CommandeFournisseurDTO> dtos = service.getAllCommandes();
        return ResponseEntity.ok(dtos);
    }
    @Operation(summary = "Get command by ID", description = "Retrieve a command by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommandeFournisseurDTO> getCommandeById(@PathVariable Long id) {
        return service.getCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Create new command", description = "Add a new command to the system")
    @PostMapping
    public ResponseEntity<CommandeFournisseurDTO> createCommande(
            @Valid @RequestBody CommandeFournisseurDTO dto) {
        CommandeFournisseurDTO created = service.createCommande(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @Operation(summary = "Get all commands with pagination support", description = "Retrieve commands with pagination support")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllPaged(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String statut) {

        Page<CommandeFournisseurDTO> page = service.findAllPaged(search, statut, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());

        return ResponseEntity.ok(response);
    }





    @PutMapping("/{id}")
    public ResponseEntity<CommandeFournisseurDTO> updateCommande(
            @PathVariable Long id,
            @Valid @RequestBody CommandeFournisseurDTO dto) {
        CommandeFournisseurDTO updated = service.updateCommande(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        try {
            service.deleteCommande(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
