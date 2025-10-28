package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.repository.ProduitRepository;
import com.tricol.Tricol.service.ProduitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }
}
