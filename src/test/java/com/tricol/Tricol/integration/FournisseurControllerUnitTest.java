package com.tricol.Tricol.integration;
import com.tricol.Tricol.controller.FournisseurController;
import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.service.FournisseurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FournisseurControllerUnitTest {

    @Mock
    private FournisseurService fournisseurService;

    @InjectMocks
    private FournisseurController controller;

    private Fournisseur fournisseur1;
    private Fournisseur fournisseur2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Example Fournisseurs with all required fields
        fournisseur1 = new Fournisseur();
        fournisseur1.setId(1L);
        fournisseur1.setSociete("Societe1");
        fournisseur1.setAdresse("Adresse1");
        fournisseur1.setContact("Contact1");
        fournisseur1.setEmail("email1@test.com");
        fournisseur1.setTelephone("0661234567");
        fournisseur1.setVille("Ville1");
        fournisseur1.setIce("ICE1");

        fournisseur2 = new Fournisseur();
        fournisseur2.setId(2L);
        fournisseur2.setSociete("Societe2");
        fournisseur2.setAdresse("Adresse2");
        fournisseur2.setContact("Contact2");
        fournisseur2.setEmail("email2@test.com");
        fournisseur2.setTelephone("0667654321");
        fournisseur2.setVille("Ville2");
        fournisseur2.setIce("ICE2");
    }

    // ---------------- Get All ----------------
    @Test
    void testGetAll() {
        when(fournisseurService.findAll()).thenReturn(List.of(fournisseur1, fournisseur2));

        ResponseEntity<List<Fournisseur>> response = controller.getAll();

        assertEquals(2, response.getBody().size());
        assertEquals("Societe1", response.getBody().get(0).getSociete());
        verify(fournisseurService, times(1)).findAll();
    }

    // ---------------- Get All Paged ----------------
    @Test
    void testGetAllPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Fournisseur> page = new PageImpl<>(List.of(fournisseur1, fournisseur2));

        when(fournisseurService.findAllPaged(pageable, null)).thenReturn(page);

        ResponseEntity<Map<String, Object>> response = controller.getAllPaged(pageable, null);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, ((List<?>) body.get("content")).size());
        assertEquals(1, body.get("totalPages")); // Only 1 page
    }

    // ---------------- Get By Id ----------------
    @Test
    void testGetByIdFound() {
        when(fournisseurService.findById(1L)).thenReturn(Optional.of(fournisseur1));

        ResponseEntity<Fournisseur> response = controller.getById(1L);

        assertEquals(fournisseur1, response.getBody());
    }

    @Test
    void testGetByIdNotFound() {
        when(fournisseurService.findById(3L)).thenReturn(Optional.empty());

        ResponseEntity<Fournisseur> response = controller.getById(3L);

        assertEquals(404, response.getStatusCodeValue());
    }

    // ---------------- Create ----------------
    @Test
    void testCreate() {
        FournisseurDTO dto = new FournisseurDTO();
        dto.setSociete("Societe3");
        dto.setAdresse("Adresse3");
        dto.setContact("Contact3");
        dto.setEmail("email3@test.com");
        dto.setTelephone("0660000000");
        dto.setVille("Ville3");
        dto.setIce("ICE3");

        Fournisseur created = new Fournisseur();
        created.setId(3L);
        created.setSociete(dto.getSociete());
        created.setAdresse(dto.getAdresse());
        created.setContact(dto.getContact());
        created.setEmail(dto.getEmail());
        created.setTelephone(dto.getTelephone());
        created.setVille(dto.getVille());
        created.setIce(dto.getIce());

        when(fournisseurService.create(dto)).thenReturn(created);

        ResponseEntity<Fournisseur> response = controller.create(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Societe3", response.getBody().getSociete());
    }

    // ---------------- Update ----------------
    @Test
    void testUpdateFound() {
        FournisseurDTO dto = new FournisseurDTO();
        dto.setSociete("UpdatedSociete");
        dto.setAdresse("UpdatedAdresse");
        dto.setContact("UpdatedContact");
        dto.setEmail("updated@test.com");
        dto.setTelephone("0669999999");
        dto.setVille("UpdatedVille");
        dto.setIce("UpdatedICE");

        Fournisseur updated = new Fournisseur();
        updated.setId(1L);
        updated.setSociete(dto.getSociete());
        updated.setAdresse(dto.getAdresse());
        updated.setContact(dto.getContact());
        updated.setEmail(dto.getEmail());
        updated.setTelephone(dto.getTelephone());
        updated.setVille(dto.getVille());
        updated.setIce(dto.getIce());

        when(fournisseurService.update(1L, dto)).thenReturn(updated);

        ResponseEntity<Fournisseur> response = controller.update(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UpdatedSociete", response.getBody().getSociete());
    }

    @Test
    void testUpdateNotFound() {
        FournisseurDTO dto = new FournisseurDTO();
        when(fournisseurService.update(5L, dto)).thenReturn(null);

        ResponseEntity<Fournisseur> response = controller.update(5L, dto);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        doNothing().when(fournisseurService).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(fournisseurService, times(1)).delete(1L);
    }
}
