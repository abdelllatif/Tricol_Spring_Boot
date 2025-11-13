package com.tricol.Tricol.integration;

import com.tricol.Tricol.controller.CategorieController;
import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.model.Categorie;
import com.tricol.Tricol.service.CategorieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategorieControllerUnitTest {


@InjectMocks
    private CategorieController controller;
@Mock
    private CategorieService service;
    private CategorieDTO categorieDTO;

    private Categorie categorie1;
    private Categorie categorie2;
    @BeforeEach void setUp() {
        categorie1 = new Categorie();
        categorie2 = new Categorie();
        categorie1.setId(1L);
        categorie1.setNom("Smartphone");
        categorie2.setId(2L);
        categorie2.setNom("Laptop");
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
}
