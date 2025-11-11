package com.tricol.Tricol.service;
import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.repository.ProduitRepository;
import com.tricol.Tricol.service.Implementation.ProduitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository repository;

    @InjectMocks
    private ProduitServiceImpl service;

    @Test
    void testFindById() {
        Produit p = new Produit();
        p.setId(1L);
        p.setNom("Chemise");
        p.setPrixUnitaire(100);

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<ProduitDTO> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Chemise", result.get().getNom());
        verify(repository, times(1)).findById(1L);
    }
}
