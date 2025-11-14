package com.tricol.Tricol.Unit.Service;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import com.tricol.Tricol.dto.CommandeProduitDTO;
import com.tricol.Tricol.enums.StatutCommande;
import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.mapper.CommandeFournisseurMapper;
import com.tricol.Tricol.model.*;
import com.tricol.Tricol.repository.*;
import com.tricol.Tricol.service.Implementation.CommandeFournisseurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandeFournisseurServiceImplTest {

    @InjectMocks
    private CommandeFournisseurServiceImpl service;

    @Mock
    private CommandeFournisseurRepository repository;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private MouvementStockRepository mouvementStockRepository;

    @Mock
    private StockCUMPRepository stockCUMPRepository;

    @Mock
    private CommandeFournisseurMapper mapper;

    private CommandeFournisseurDTO dto;
    private CommandeFournisseur entity;
    private Fournisseur fournisseur;
    private Produit produitEntity;
    private CommandeProduit cp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fournisseur = new Fournisseur();
        fournisseur.setId(1L);
        fournisseur.setSociete("Test Societe");

        produitEntity = new Produit();
        produitEntity.setId(1L);
        produitEntity.setNom("Produit Test");
        produitEntity.setPrixUnitaire(50.0);
        produitEntity.setStockActuel(100);

        cp = new CommandeProduit();
        cp.setProduit(produitEntity);
        cp.setQuantite(10);

        entity = new CommandeFournisseur();
        entity.setId(1L);
        entity.setFournisseur(fournisseur);
        entity.setStatut(StatutCommande.EN_ATTENTE);
        entity.getProduits().add(cp);

        dto = new CommandeFournisseurDTO();
        dto.setFournisseurId(1L);
        dto.setStatut(StatutCommande.EN_ATTENTE);
        CommandeProduitDTO cpDto = new CommandeProduitDTO();
        cpDto.setProduitId(1L);
        cpDto.setQuantite(10);
        dto.setProduits(List.of(cpDto));

        when(mapper.toDTO(any(CommandeFournisseur.class))).thenAnswer(i -> {
            CommandeFournisseur c = i.getArgument(0);
            CommandeFournisseurDTO d = new CommandeFournisseurDTO();
            d.setFournisseurId(c.getFournisseur().getId());
            d.setStatut(c.getStatut());
            return d;
        });
    }

    @Test
    void createCommande_ShouldSaveAndReturnDTO() {
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produitEntity));
        when(repository.save(any())).thenReturn(entity);

        CommandeFournisseurDTO result = service.createCommande(dto);

        assertNotNull(result);
        assertEquals(1L, result.getFournisseurId());
        verify(repository).save(any());
    }

    @Test
    void updateCommande_ShouldUpdateAndReturnDTO() {
        dto.setStatut(StatutCommande.LIVREE);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur));

        // Mock StockCUMP to avoid NullPointer
        StockCUMP stockCUMP = new StockCUMP();
        stockCUMP.setProduit(produitEntity);
        stockCUMP.setCoutUnitaireCUMP(50.0);
        when(stockCUMPRepository.findByProduitId(1L)).thenReturn(Optional.of(stockCUMP));
        when(stockCUMPRepository.save(any())).thenReturn(stockCUMP);

        when(produitRepository.save(any())).thenReturn(produitEntity);
        when(mouvementStockRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(repository.save(any(CommandeFournisseur.class))).thenReturn(entity);

        CommandeFournisseurDTO result = service.updateCommande(1L, dto);

        assertNotNull(result);
        verify(mouvementStockRepository, atLeastOnce()).save(any());
        verify(produitRepository, atLeastOnce()).save(any());
        verify(stockCUMPRepository, atLeastOnce()).save(any());
    }

    @Test
    void findAllPaged_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CommandeFournisseur> page = new PageImpl<>(List.of(entity));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<CommandeFournisseurDTO> result = service.findAllPaged(null, null, pageable);
        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    void deleteCommande_ShouldDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        service.deleteCommande(1L);
        verify(repository).delete(entity);
    }

    @Test
    void getAllCommandes_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(entity));
        List<CommandeFournisseurDTO> result = service.getAllCommandes();
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void getCommandeById_ShouldReturnOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Optional<CommandeFournisseurDTO> result = service.getCommandeById(1L);
        assertTrue(result.isPresent());
        verify(repository).findById(1L);
    }
}
