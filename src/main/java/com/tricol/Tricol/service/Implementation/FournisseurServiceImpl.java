package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.model.Fournisseur;
import com.tricol.Tricol.repository.FournisseurRepository;
import com.tricol.Tricol.service.FournisseurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    public  FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Optional<Fournisseur> findById(Long id) {
        return fournisseurRepository.findById(id);
    }
    @Override
    public Page<Fournisseur> findAllPaged(Pageable pageable) {
        return fournisseurRepository.findAll(pageable);
    }
    @Override
    public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
