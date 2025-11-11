package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import com.tricol.Tricol.model.CommandeFournisseur;
import com.tricol.Tricol.repository.CommandeFournisseurRepository;
import com.tricol.Tricol.service.Implementation.CommandeFournisseurServiceImpl;
import com.tricol.Tricol.mapper.CommandeFournisseurMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandeFournisseurServiceTest {

    @Mock
    private CommandeFournisseurRepository repository;

    @Mock
    private CommandeFournisseurMapper mapper;

    @InjectMocks
    private CommandeFournisseurServiceImpl service;

    @Test
    void testGetCommandeById() {
        CommandeFournisseur cmd = new CommandeFournisseur();
        cmd.setId(1L);

        CommandeFournisseurDTO dto = new CommandeFournisseurDTO();
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(cmd));
        when(mapper.toDTO(cmd)).thenReturn(dto);

        Optional<CommandeFournisseurDTO> result = service.getCommandeById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(cmd);
    }
}
