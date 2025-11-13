package com.tricol.Tricol.integration;

import com.tricol.Tricol.controller.ProduitController;
import com.tricol.Tricol.dto.MouvementStockDTO;
import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduitControllerUnitTest {

    @Mock
    private ProduitService produitService;

    @InjectMocks
    private ProduitController produitController;

    private ProduitDTO sampleProduit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProduit = new ProduitDTO();
        sampleProduit.setId(1L);
        sampleProduit.setNom("Laptop");
    }

    // ---------------- Positive Tests ----------------

    @Test
    void getAllProduits_shouldReturnList() {
        when(produitService.findAll()).thenReturn(List.of(sampleProduit));

        ResponseEntity<List<ProduitDTO>> response = produitController.getAllProduits();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).getNom());
    }

    @Test
    void getProduitById_shouldReturnProduit() {
        when(produitService.findById(1L)).thenReturn(Optional.of(sampleProduit));

        ResponseEntity<ProduitDTO> response = produitController.getProduitById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getNom());
    }

    @Test
    void createProduit_shouldReturnCreated() {
        when(produitService.create(any(ProduitDTO.class), eq(1000.0))).thenReturn(sampleProduit);

        ResponseEntity<ProduitDTO> response = produitController.createProduit(sampleProduit, 1000.0);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getNom());
    }

    @Test
    void updateProduit_shouldReturnUpdated() {
        when(produitService.update(eq(1L), any(ProduitDTO.class))).thenReturn(sampleProduit);

        ResponseEntity<ProduitDTO> response = produitController.updateProduit(1L, sampleProduit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getNom());
    }

    @Test
    void deleteProduit_shouldReturnNoContent() {
        doNothing().when(produitService).delete(1L);

        ResponseEntity<Void> response = produitController.deleteProduit(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(produitService, times(1)).delete(1L);
    }

    @Test
    void ajusterStock_shouldReturnUpdated() {
        MouvementStockDTO mouvement = new MouvementStockDTO();
        mouvement.setQuantite(5);
        mouvement.setCoutUnitaire(100.0);
        mouvement.setTypeMouvement(TypeMouvement.ENTREE);

        when(produitService.ajusterStock(eq(1L), eq(5), eq(100.0), eq(TypeMouvement.ENTREE)))
                .thenReturn(sampleProduit);

        ResponseEntity<ProduitDTO> response = produitController.ajusterStock(1L, mouvement);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Laptop", response.getBody().getNom());
    }


    // ---------------- Negative Tests ----------------

    @Test
    void getProduitById_shouldReturnNotFound() {
        when(produitService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProduitDTO> response = produitController.getProduitById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updateProduit_shouldReturnNotFound() {
        when(produitService.update(eq(1L), any(ProduitDTO.class))).thenReturn(null);

        ResponseEntity<ProduitDTO> response = produitController.updateProduit(1L, sampleProduit);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void ajusterStock_shouldReturnBadRequest_onException() {
        MouvementStockDTO mouvement = new MouvementStockDTO();
        mouvement.setQuantite(5);
        mouvement.setCoutUnitaire(100.0); // must set this, otherwise double is 0
        mouvement.setTypeMouvement(TypeMouvement.ENTREE);

        // Match the exact parameters passed to the service
        when(produitService.ajusterStock(
                eq(1L),
                eq(5),
                eq(100.0),
                eq(TypeMouvement.ENTREE))
        ).thenThrow(new RuntimeException("Produit non trouvé"));

        ResponseEntity<ProduitDTO> response = produitController.ajusterStock(1L, mouvement);

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
    @Test
    void getAllPaged_shouldReturnPageWithContent() {
        // Prepare mock data
        ProduitDTO p1 = new ProduitDTO();
        p1.setId(1L);
        p1.setNom("Laptop");

        ProduitDTO p2 = new ProduitDTO();
        p2.setId(2L);
        p2.setNom("Phone");

        List<ProduitDTO> content = List.of(p1, p2);
        Page<ProduitDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), content.size());

        // Mock service
        when(produitService.findAllPaged(eq("Lap"), any())).thenReturn(page);

        // Call controller
        ResponseEntity<Map<String, Object>> response = produitController.getAllPaged(PageRequest.of(0,10), "Lap");

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, ((List<?>)body.get("content")).size());
        assertEquals(1, body.get("totalPages"));
        assertEquals(2L, body.get("totalElements"));
        assertEquals(0, body.get("number"));
        assertEquals(10, body.get("size"));
    }

    @Test
    void getAllPaged_shouldReturnEmptyPage() {
        // Empty page
        Page<ProduitDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0,10), 0);

        when(produitService.findAllPaged(isNull(), any())).thenReturn(emptyPage);

        ResponseEntity<Map<String, Object>> response = produitController.getAllPaged(PageRequest.of(0,10), null);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(((List<?>) body.get("content")).isEmpty());
        assertEquals(0, body.get("totalPages"));
        assertEquals(0L, body.get("totalElements"));
    }

}
