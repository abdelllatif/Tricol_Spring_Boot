package com.tricol.Tricol.Unit.Service;

import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.mapper.FournisseurMapper;
import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.repository.FournisseurRepository;
import com.tricol.Tricol.service.Implementation.FournisseurServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FournisseurServiceImpl Unit Tests - 100% Coverage")
class FournisseurServiceImplTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private FournisseurMapper fournisseurMapper;

    @InjectMocks
    private FournisseurServiceImpl fournisseurService;

    private Fournisseur fournisseur;
    private FournisseurDTO dto;
    private final Long ID = 1L;
    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    void setUp() {
        fournisseur = new Fournisseur();
        fournisseur.setId(ID);
        fournisseur.setSociete("SOC TEST");
        fournisseur.setIce("123456789012345");
        fournisseur.setVille("Casablanca");

        dto = new FournisseurDTO();
        dto.setSociete("SOC TEST");
        dto.setAdresse("123 Rue Test");
        dto.setContact("Mr Contact");
        dto.setEmail("test@soc.com");
        dto.setTelephone("0600000000");
        dto.setVille("Casablanca");
        dto.setIce("123456789012345");
    }

    // ========================================================================
    // findById
    // ========================================================================
    @Test
    @DisplayName("findById → returns Fournisseur when exists")
    void findById_whenExists_returnsFournisseur() {
        when(fournisseurRepository.findById(ID)).thenReturn(Optional.of(fournisseur));

        Optional<Fournisseur> result = fournisseurService.findById(ID);

        assertTrue(result.isPresent());
        assertEquals(fournisseur, result.get());
        verify(fournisseurRepository).findById(ID);
    }

    @Test
    @DisplayName("findById → returns empty when not exists")
    void findById_whenNotExists_returnsEmpty() {
        when(fournisseurRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Fournisseur> result = fournisseurService.findById(ID);

        assertFalse(result.isPresent());
        verify(fournisseurRepository).findById(ID);
    }

    // ========================================================================
    // findAll
    // ========================================================================
    @Test
    @DisplayName("findAll → returns list of all fournisseurs")
    void findAll_returnsAllFournisseurs() {
        List<Fournisseur> list = Arrays.asList(fournisseur, new Fournisseur());
        when(fournisseurRepository.findAll()).thenReturn(list);

        List<Fournisseur> result = fournisseurService.findAll();

        assertEquals(2, result.size());
        verify(fournisseurRepository).findAll();
    }

    // ========================================================================
    // findAllPaged
    // ========================================================================
    @Nested
    @DisplayName("findAllPaged tests")
    class FindAllPagedTests {

        private Page<Fournisseur> page;

        @BeforeEach
        void pageSetup() {
            page = new PageImpl<>(List.of(fournisseur), pageable, 1);
        }

        @Test
        @DisplayName("with search term → calls search repository method")
        void withSearchTerm_callsSearchMethod() {
            when(fournisseurRepository.findBySocieteContainingIgnoreCaseOrIceContainingOrVilleContainingIgnoreCase(
                    anyString(), anyString(), anyString(), any(Pageable.class)))
                    .thenReturn(page);

            Page<Fournisseur> result = fournisseurService.findAllPaged(pageable, "casa");

            assertEquals(page, result);
            verify(fournisseurRepository)
                    .findBySocieteContainingIgnoreCaseOrIceContainingOrVilleContainingIgnoreCase(
                            "casa", "casa", "casa", pageable);
            verify(fournisseurRepository, never()).findAll(pageable);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("with null or blank search → calls findAll(pageable)")
        void withNullOrBlankSearch_callsFindAll(String search) {
            when(fournisseurRepository.findAll(pageable)).thenReturn(page);

            Page<Fournisseur> result = fournisseurService.findAllPaged(pageable, search);

            assertEquals(page, result);
            verify(fournisseurRepository, never())
                    .findBySocieteContainingIgnoreCaseOrIceContainingOrVilleContainingIgnoreCase(any(), any(), any(), any());
            verify(fournisseurRepository).findAll(pageable);
        }
    }

    // ========================================================================
    // delete
    // ========================================================================
    @Test
    @DisplayName("delete → calls repository deleteById")
    void delete_callsRepositoryDelete() {
        fournisseurService.delete(ID);

        verify(fournisseurRepository).deleteById(ID);
    }

    // ========================================================================
    // create
    // ========================================================================
    @Test
    @DisplayName("create → maps DTO to entity and saves")
    void create_mapsAndSaves() {
        when(fournisseurMapper.toEntity(dto)).thenReturn(fournisseur);
        when(fournisseurRepository.save(fournisseur)).thenReturn(fournisseur);

        Fournisseur result = fournisseurService.create(dto);

        assertEquals(fournisseur, result);
        verify(fournisseurMapper).toEntity(dto);
        verify(fournisseurRepository).save(fournisseur);
    }

    // ========================================================================
    // update
    // ========================================================================
    @Nested
    @DisplayName("update tests")
    class UpdateTests {

        @Test
        @DisplayName("when fournisseur exists → updates all fields and returns entity")
        void whenExists_updatesAndReturnsEntity() {
            when(fournisseurRepository.findById(ID)).thenReturn(Optional.of(fournisseur));
            when(fournisseurRepository.save(fournisseur)).thenReturn(fournisseur);

            Fournisseur result = fournisseurService.update(ID, dto);

            assertNotNull(result);
            assertEquals(dto.getSociete(), result.getSociete());
            assertEquals(dto.getAdresse(), result.getAdresse());
            assertEquals(dto.getContact(), result.getContact());
            assertEquals(dto.getEmail(), result.getEmail());
            assertEquals(dto.getTelephone(), result.getTelephone());
            assertEquals(dto.getVille(), result.getVille());
            assertEquals(dto.getIce(), result.getIce());

            verify(fournisseurRepository).findById(ID);
            verify(fournisseurRepository).save(fournisseur);
        }

        @Test
        @DisplayName("when fournisseur does NOT exist → returns null")
        void whenNotExists_returnsNull() {
            when(fournisseurRepository.findById(ID)).thenReturn(Optional.empty());

            Fournisseur result = fournisseurService.update(ID, dto);

            assertNull(result);
            verify(fournisseurRepository).findById(ID);
            verify(fournisseurRepository, never()).save(any());
        }
    }
}