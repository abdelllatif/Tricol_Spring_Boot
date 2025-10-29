package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.mapper.ProduitMapper;
import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.repository.ProduitRepository;
import com.tricol.Tricol.service.ProduitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ProduitServiceImpl(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }


    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    public Produit create(ProduitDTO dto) {
        Produit produit = produitMapper.toEntity(dto);
        return produitRepository.save(produit);
    }

    public Produit update(Long id, ProduitDTO dto) {
        return produitRepository.findById(id).map(existing -> {
            Produit updated = produitMapper.toEntity(dto);
            updated.setId(id);
            return produitRepository.save(updated);
        }).orElse(null);
    }
}
