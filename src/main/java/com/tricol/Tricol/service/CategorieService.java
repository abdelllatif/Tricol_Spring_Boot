package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.CategorieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategorieService {
    List<CategorieDTO> findAll();
    Page<CategorieDTO> findAllPaged(String nom, Pageable pageable) ;
    CategorieDTO create(CategorieDTO dto);
    CategorieDTO update(Long id, CategorieDTO dto);
    void delete(Long id);
    CategorieDTO findById(Long id);
}