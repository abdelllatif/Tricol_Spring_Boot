package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.model.MouvementStock;
import com.tricol.Tricol.repository.MouvementStockRepository;
import com.tricol.Tricol.service.MouvementStockService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository repository;

    public MouvementStockServiceImpl(MouvementStockRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MouvementStock> getAllMouvements() {
        return repository.findAll();
    }

    @Override
    public Optional<MouvementStock> getMouvementById(Long id) {
        return repository.findById(id);
    }

    @Override
    public MouvementStock createMouvement(MouvementStock mouvementStock) {
        mouvementStock.setCreatedAt(LocalDateTime.now());
        mouvementStock.setUpdatedAt(LocalDateTime.now());
        return repository.save(mouvementStock);
    }

    @Override
    public MouvementStock updateMouvement(Long id, MouvementStock mouvementStock) {
        return repository.findById(id).map(existing -> {
            existing.setProduit(mouvementStock.getProduit());
            existing.setCommande(mouvementStock.getCommande());
            existing.setTypeMouvement(mouvementStock.getTypeMouvement());
            existing.setQuantite(mouvementStock.getQuantite());
            existing.setCoutUnitaire(mouvementStock.getCoutUnitaire());
            existing.setRemarque(mouvementStock.getRemarque());
            existing.setDateMouvement(mouvementStock.getDateMouvement());
            existing.setUpdatedAt(LocalDateTime.now());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("MouvementStock not found with id " + id));
    }

    @Override
    public void deleteMouvement(Long id) {
        repository.deleteById(id);
    }
}
