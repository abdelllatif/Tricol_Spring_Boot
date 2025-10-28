package com.tricol.Tricol.controller;


import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.service.FournisseurService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public List<Fournisseur> getAll() {
        return fournisseurService.findAll();
    }
    @GetMapping("/page")
    public Page<Fournisseur> getAllPaged(Pageable pageable) {
        return fournisseurService.findAllPaged(pageable);
    }
    @PostMapping
    public Fournisseur create(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.save(fournisseur);
    }

    @GetMapping("/{id}")
    public Fournisseur getById(@PathVariable Long id) {
        return fournisseurService.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        fournisseurService.delete(id);
    }
}
