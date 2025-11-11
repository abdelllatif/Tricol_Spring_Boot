package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.CategorieDTO;
import com.tricol.Tricol.mapper.CategorieMapper;
import com.tricol.Tricol.model.Categorie;
import com.tricol.Tricol.repository.CategorieRepository;
import com.tricol.Tricol.service.CategorieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository repo;
    private final CategorieMapper mapper;

    public CategorieServiceImpl(CategorieRepository repo, CategorieMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<CategorieDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public Page<CategorieDTO> findAllPaged(String nom, Pageable pageable) {
        if (nom != null && !nom.trim().isEmpty()) {
            return repo.findByNomContainingIgnoreCase(nom, pageable)
                    .map(mapper::toDTO);
        }
        return repo.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional
    public CategorieDTO create(CategorieDTO dto) {
        Categorie entity = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(entity));
    }

    @Override
    @Transactional
    public CategorieDTO update(Long id, CategorieDTO dto) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setNom(dto.getNom());
                    return mapper.toDTO(repo.save(existing));
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public CategorieDTO findById(Long id) {
        return repo.findById(id).map(mapper::toDTO).orElse(null);
    }
}