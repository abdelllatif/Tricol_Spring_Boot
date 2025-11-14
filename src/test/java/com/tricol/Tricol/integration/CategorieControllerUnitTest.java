package com.tricol.Tricol.integration;

import com.tricol.Tricol.controller.CategorieController;
import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.model.Categorie;
import com.tricol.Tricol.service.CategorieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategorieControllerUnitTest {


@InjectMocks
    private CategorieController controller;
@Mock
    private CategorieService service;
    private CategorieDTO categorieDTO;
    private CategorieDTO categorie1DTO;
    private CategorieDTO categorie2DTO;
    @BeforeEach void setUp() {
        MockitoAnnotations.openMocks(this);
        categorieDTO = new CategorieDTO();
        categorieDTO.setId(3L);
        categorieDTO.setNom("jeans");

    }


    @Test
    public void testCreate() {
        when(service.create(any(CategorieDTO.class))).thenReturn(categorieDTO);
        ResponseEntity<CategorieDTO> response=controller.create(categorieDTO);
        assertEquals(201 , response.getStatusCodeValue());
        assertEquals("jeans",response.getBody().getNom());
        assertEquals(3L,response.getBody().getId());
        verify(service).create(any(CategorieDTO.class));
    }

    @Test
    public void testOfGettingAllCategrie_ShouldReturnList() {
        when(service.findAll()).thenReturn(List.of(categorieDTO));
        ResponseEntity<List<CategorieDTO>> resp=controller.getAll();
        assertEquals(200,resp.getStatusCodeValue());
        assertEquals(1,resp.getBody().size());
        assertEquals("jeans",resp.getBody().get(0).getNom());
    }
    @Test
    public void testOfGettingCategorieById_ShouldReturnCategorie(){
        when(service.findById(3L)).thenReturn(categorieDTO);
        ResponseEntity<CategorieDTO> resp=controller.getById(3L);
        assertEquals("jeans",resp.getBody().getNom());
    }
    @Test
    public void testOfGettingCategorieById_ShouldReturnNotfound(){
        when(service.findById(7L)).thenReturn(null);
        ResponseEntity<CategorieDTO> resp=controller.getById(7L);
        assertEquals(404,resp.getStatusCodeValue());
    }

    @Test
    void updateProduit_shouldReturnUpdated() {
        when(service.update(eq(3L), any(CategorieDTO.class))).thenReturn(categorieDTO);

        ResponseEntity<CategorieDTO> resp = controller.update(3L, categorieDTO);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("jeans", resp.getBody().getNom());
        verify(service).update(eq(3L), any(CategorieDTO.class));
    }


}
