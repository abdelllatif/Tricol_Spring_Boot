package com.tricol.Tricol.service;
import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.repository.FournisseurRepository;
import com.tricol.Tricol.service.Implementation.FournisseurServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock
    private FournisseurRepository repository;

    @InjectMocks
    private FournisseurServiceImpl service;

    @Test
    void testFindById() {
        Fournisseur f = new Fournisseur();
        f.setId(1L);
        f.setSociete("Tricol");

        when(repository.findById(1L)).thenReturn(Optional.of(f));

        Optional<Fournisseur> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Tricol", result.get().getSociete());
        verify(repository, times(1)).findById(1L);
    }
}
