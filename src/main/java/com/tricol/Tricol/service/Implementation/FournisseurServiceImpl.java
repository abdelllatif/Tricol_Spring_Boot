package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.FournisseurDTO;
import com.tricol.Tricol.mapper.FournisseurMapper;
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
    private final FournisseurMapper fournisseurMapper;

    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }


    @Override
    public Optional<Fournisseur> findById(Long id) {
        return fournisseurRepository.findById(id);
    }

    @Override
    public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }

    @Override
    public Page<Fournisseur> findAllPaged(Pageable pageable) {
        return fournisseurRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        fournisseurRepository.deleteById(id);
    }

    @Override
    public Fournisseur create(FournisseurDTO dto) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(dto);
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Fournisseur update(Long id, FournisseurDTO dto) {
        Optional<Fournisseur> existing = fournisseurRepository.findById(id);
        if (existing.isPresent()) {
            Fournisseur fournisseur = existing.get();
            // Update fields from DTO
            fournisseur.setSociete(dto.getSociete());
            fournisseur.setAdresse(dto.getAdresse());
            fournisseur.setContact(dto.getContact());
            fournisseur.setEmail(dto.getEmail());
            fournisseur.setTelephone(dto.getTelephone());
            fournisseur.setVille(dto.getVille());
            fournisseur.setIce(dto.getIce());

            return fournisseurRepository.save(fournisseur);
        } else {
            return null;
        }
    }
}
