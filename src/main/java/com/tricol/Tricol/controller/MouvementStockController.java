package com.tricol.Tricol.controller;

import com.tricol.Tricol.model.MouvementStock;
import com.tricol.Tricol.service.MouvementStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements-stock")
@CrossOrigin(origins = "*")
public class MouvementStockController {

    private final MouvementStockService service;

    public MouvementStockController(MouvementStockService service) {
        this.service = service;
    }

    @GetMapping
    public List<MouvementStock> getAllMouvements() {
        return service.getAllMouvements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouvementStock> getMouvementById(@PathVariable Long id) {
        return service.getMouvementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MouvementStock createMouvement(@RequestBody MouvementStock mouvementStock) {
        return service.createMouvement(mouvementStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MouvementStock> updateMouvement(@PathVariable Long id,
                                                          @RequestBody MouvementStock mouvementStock) {
        try {
            return ResponseEntity.ok(service.updateMouvement(id, mouvementStock));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        service.deleteMouvement(id);
        return ResponseEntity.noContent().build();
    }
}
