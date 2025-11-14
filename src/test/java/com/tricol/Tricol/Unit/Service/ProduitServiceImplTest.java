package com.tricol.Tricol.Unit.Service;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.mapper.ProduitMapper;
import com.tricol.Tricol.model.MouvementStock;
import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.model.StockCUMP;
import com.tricol.Tricol.repository.MouvementStockRepository;
import com.tricol.Tricol.repository.ProduitRepository;
import com.tricol.Tricol.repository.StockCUMPRepository;
import com.tricol.Tricol.service.Implementation.ProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProduitServiceImplTest {

    @InjectMocks
    private ProduitServiceImpl service;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ProduitMapper produitMapper;

    @Mock
    private MouvementStockRepository mouvementStockRepository;

    @Mock
    private StockCUMPRepository stockCUMPRepository;

    private Produit produit;
    private ProduitDTO produitDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Test Produit");
        produit.setStockActuel(100);
        produit.setPrixUnitaire(50.0);

        produitDTO = new ProduitDTO();
        produitDTO.setId(1L);
        produitDTO.setNom("Test Produit");
        produitDTO.setPrixUnitaire(50.0);

        // Mapper behavior
        when(produitMapper.toEntity(any(ProduitDTO.class))).thenReturn(produit);
        when(produitMapper.toDTO(any(Produit.class))).thenReturn(produitDTO);
    }

    // ===================== CREATE =====================
    @Test
    void create_WithValidData_ShouldSaveAllAndReturnDTO() {
        Double prixAchat = 45.0;

        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(stockCUMPRepository.save(any(StockCUMP.class))).thenAnswer(i -> i.getArgument(0));
        when(mouvementStockRepository.save(any(MouvementStock.class))).thenAnswer(i -> i.getArgument(0));

        ProduitDTO result = service.create(produitDTO, prixAchat);

        assertNotNull(result);
        verify(produitRepository).save(produit);
        verify(stockCUMPRepository).save(argThat(s -> s.getCoutUnitaireCUMP().equals(prixAchat)));
        verify(mouvementStockRepository).save(argThat(m ->
                m.getTypeMouvement() == TypeMouvement.ENTREE &&
                        m.getQuantite() == 100 && // stock initial
                        m.getCoutUnitaire().equals(prixAchat)
        ));
    }

    @Test
    void create_NullPrixAchat_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> service.create(produitDTO, null));
        verify(produitRepository, never()).save(any());
    }


//all
    @Test
    void findAll_ShouldReturnListOfDTOs() {
        Produit p = new Produit(); p.setId(1L); p.setNom("P");
        ProduitDTO dto = new ProduitDTO(); dto.setId(1L); dto.setNom("P");

        when(produitRepository.findAll()).thenReturn(List.of(p));
        when(produitMapper.toDTO(p)).thenReturn(dto);

        List<ProduitDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("P", result.get(0).getNom());
    }


    // ===================== UPDATE =====================
    @Test
    void update_WhenExists_ShouldUpdateAndReturnDTO() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitRepository.save(produit)).thenReturn(produit);

        produitDTO.setPrixUnitaire(99.99);
        produitDTO.setNom("Nouveau Nom");

        ProduitDTO result = service.update(1L, produitDTO);

        assertEquals("Nouveau Nom", produit.getNom());
        assertEquals(99.99, produit.getPrixUnitaire());
        assertEquals(result, produitDTO);
        verify(produitRepository).save(produit);
    }

    @Test
    void update_WhenNotFound_ShouldReturnNull() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        ProduitDTO result = service.update(99L, produitDTO);

        assertNull(result);
        verify(produitRepository, never()).save(any());
    }

    // ===================== AJUSTER STOCK - ENTREE =====================
    @Test
    void ajusterStock_Entree_ShouldUpdateStockAndCUMP() {
        StockCUMP existingCump = new StockCUMP();
        existingCump.setCoutUnitaireCUMP(40.0);

        MouvementStock oldMvt = new MouvementStock();
        oldMvt.setQuantite(50);
        oldMvt.setCoutUnitaire(40.0);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockCUMPRepository.findByProduitId(1L)).thenReturn(Optional.of(existingCump));
        when(mouvementStockRepository.findByProduitIdAndTypeMouvement(1L, TypeMouvement.ENTREE))
                .thenReturn(List.of(oldMvt));
        when(mouvementStockRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(stockCUMPRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.ajusterStock(1L, 50, 60.0, TypeMouvement.ENTREE);

        // Vérification CUMP : (50*40 + 50*60) / (50 + 50) = 50
        assertEquals(50.0, existingCump.getCoutUnitaireCUMP(), 0.001);
        assertEquals(150, produit.getStockActuel()); // 100 + 50
        verify(produitRepository).save(produit);
        verify(mouvementStockRepository).save(argThat(m ->
                m.getQuantite() == 50 && m.getCoutUnitaire().equals(60.0)
        ));
    }

    @Test
    void ajusterStock_Entree_FirstTime_ShouldCreateStockCUMP() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockCUMPRepository.findByProduitId(1L)).thenReturn(Optional.empty());
        when(mouvementStockRepository.findByProduitIdAndTypeMouvement(1L, TypeMouvement.ENTREE))
                .thenReturn(List.of());

        ProduitDTO result = service.ajusterStock(1L, 5, 30.0, TypeMouvement.ENTREE);

        verify(stockCUMPRepository).save(argThat(s -> s.getCoutUnitaireCUMP() == 30.0));
        assertEquals(105, produit.getStockActuel()); // 100 + 5
    }

    @Test
    void ajusterStock_Sortie_ShouldOnlyUpdateStockAndSaveMouvement() {
        produit.setStockActuel(100);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        service.ajusterStock(1L, 30, 55.0, TypeMouvement.SORTIE);

        assertEquals(70, produit.getStockActuel());
        verify(mouvementStockRepository).save(argThat(m ->
                m.getTypeMouvement() == TypeMouvement.SORTIE && m.getQuantite() == 30
        ));
        verify(stockCUMPRepository, never()).save(any()); // Pas touché en sortie
    }

    @Test
    void ajusterStock_NegativeQuantity_ShouldThrowException() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, -5, 50.0, TypeMouvement.ENTREE));

        assertTrue(ex.getMessage().contains("Quantité invalide") || ex.getMessage().contains("Produit non trouvé"));
    }

    @Test
    void ajusterStock_NullPrixAchat_ShouldThrowException() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, 10, (Double) null, TypeMouvement.ENTREE));
    }

    @Test
    void ajusterStock_ProduitNotFound_ShouldThrowException() {
        when(produitRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.ajusterStock(999L, 10, 50.0, TypeMouvement.ENTREE));

        assertEquals("Produit non trouvé", ex.getMessage());
    }

    // ===================== FIND ALL PAGED =====================
    @Test
    void findAllPaged_WithSearch_ShouldCallSearchRepo() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Produit> page = new PageImpl<>(List.of(produit));

        when(produitRepository.findByNomContainingIgnoreCase(eq("laptop"), eq(pageable))).thenReturn(page);

        service.findAllPaged("laptop", pageable);

        verify(produitRepository).findByNomContainingIgnoreCase(eq("laptop"), eq(pageable));
    }

    @Test
    void findAllPaged_WithoutSearch_ShouldCallFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produit> page = new PageImpl<>(List.of(produit));

        when(produitRepository.findAll(pageable)).thenReturn(page);

        service.findAllPaged(null, pageable);

        verify(produitRepository).findAll(pageable);
    }

    // DELETE
    @Test
    void delete_ShouldCallDeleteById() {
        service.delete(1L);
        verify(produitRepository).deleteById(1L);
    }

    // ===================== FIND BY ID =====================
    @Test
    void findById_ShouldReturnOptionalDTO() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        Optional<ProduitDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(produitDTO, result.get());
    }




    //here i will do it

    @Test
    void ajusterStock_Entree_Valid_ShouldWork() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(stockCUMPRepository.findByProduitId(1L)).thenReturn(Optional.of(new StockCUMP()));
        when(mouvementStockRepository.findByProduitIdAndTypeMouvement(1L, TypeMouvement.ENTREE))
                .thenReturn(List.of());

        ProduitDTO result = service.ajusterStock(1L, 10, 50.0, TypeMouvement.ENTREE);

        assertNotNull(result);
        verify(produitRepository).save(any());
        verify(stockCUMPRepository).save(any());
        verify(mouvementStockRepository).save(any());
    }



    @Test
    void ajusterStock_Entree_NullPrix_ShouldThrow() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, 5, null, TypeMouvement.ENTREE));
    }

    @Test
    void ajusterStock_Entree_PrixZero_ShouldThrow() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, 5, 0.0, TypeMouvement.ENTREE));
    }

    @Test
    void ajusterStock_Sortie_Valid_ShouldWork() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        ProduitDTO result = service.ajusterStock(1L, 20, 50.0, TypeMouvement.SORTIE);

        assertEquals(80, produit.getStockActuel());
        verify(mouvementStockRepository).save(any());
    }

    @Test
    void ajusterStock_Sortie_TooMuch_ShouldThrow() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, 200, 50.0, TypeMouvement.SORTIE));
    }

    @Test
    void ajusterStock_NegativeQuantity_ShouldThrow() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(1L, -5, 50.0, TypeMouvement.ENTREE));
    }

    @Test
    void ajusterStock_ProduitNotFound_ShouldThrow() {
        when(produitRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.ajusterStock(999L, 10, 50.0, TypeMouvement.ENTREE));
    }







}