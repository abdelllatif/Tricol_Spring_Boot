package com.tricol.Tricol.controller;

import com.tricol.Tricol.dto.MouvementStockDTO;
import com.tricol.Tricol.service.MouvementStockService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mouvements")
@CrossOrigin(origins = "*")
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    public MouvementStockController(MouvementStockService mouvementStockService) {
        this.mouvementStockService = mouvementStockService;
    }

  @Operation(summary = "Get all mouvements", description = "Retrieve a list of all mouvements")
    @GetMapping
    public List<MouvementStockDTO> getAll() {
        return mouvementStockService.getAllMouvements();
    }

    @Operation(summary = "Get mouvements by produit ID", description = "Retrieve mouvements by produit ID")
    @GetMapping("/produit/{produitId}")
    public List<MouvementStockDTO> getByProduit(@PathVariable Long produitId) {
        return mouvementStockService.getMouvementsByProduit(produitId);
    }
    @Operation(summary = "Get mouvements by date", description = "Retrieve mouvements by date")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAllPaged(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) String date, // add date param
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MouvementStockDTO> mouvements = mouvementStockService.getMouvementsPaged(search, date, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("mouvements", mouvements.getContent());
        response.put("currentPage", mouvements.getNumber());
        response.put("totalItems", mouvements.getTotalElements());
        response.put("totalPages", mouvements.getTotalPages());

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get mouvements by produit ID and page", description = "Retrieve mouvements by produit ID and page")
    @GetMapping("/produit/{produitId}/page")
    public ResponseEntity<Map<String, Object>> getByProduitPaged(
            @PathVariable Long produitId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MouvementStockDTO> mouvements = mouvementStockService.getMouvementsByProduitPaged(produitId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("mouvements", mouvements.getContent());
        response.put("currentPage", mouvements.getNumber());
        response.put("totalItems", mouvements.getTotalElements());
        response.put("totalPages", mouvements.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Export mouvements to Excel", description = "Export mouvements to Excel")
    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        mouvementStockService.exportToExcel(response);
    }
}
