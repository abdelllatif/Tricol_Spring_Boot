package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.ProduitDTO;
import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.mapper.ProduitMapper;
import com.tricol.Tricol.model.MouvementStock;
import com.tricol.Tricol.model.Produit;
import com.tricol.Tricol.model.StockCUMP;
import com.tricol.Tricol.repository.MouvementStockRepository;
import com.tricol.Tricol.repository.ProduitRepository;
import com.tricol.Tricol.repository.StockCUMPRepository;
import com.tricol.Tricol.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final MouvementStockRepository mouvementStockRepository;
    private final StockCUMPRepository stockCUMPRepository;

    public ProduitServiceImpl(
            ProduitRepository produitRepository,
            ProduitMapper produitMapper,
            MouvementStockRepository mouvementStockRepository,
            StockCUMPRepository stockCUMPRepository
    ) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
        this.mouvementStockRepository = mouvementStockRepository;
        this.stockCUMPRepository = stockCUMPRepository;
    }

    @Override
    public List<ProduitDTO> findAll() {
        return produitRepository.findAll().stream().map(produitMapper::toDTO).toList();
    }

    @Override
    public Page<ProduitDTO> findAllPaged(String nom, Pageable pageable) {
        if (nom != null && !nom.trim().isEmpty()) {
            return produitRepository.findByNomContainingIgnoreCase(
                    nom, pageable).map(produitMapper::toDTO);
        }
        return produitRepository.findAll(pageable).map(produitMapper::toDTO);
    }

    @Override
    public Optional<ProduitDTO> findById(Long id) {
        return produitRepository.findById(id).map(produitMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProduitDTO create(ProduitDTO dto,Double prixAchat) {
        Produit produit = produitMapper.toEntity(dto);
        Produit savedProduit = produitRepository.save(produit);

        StockCUMP stockCUMP = new StockCUMP();
        stockCUMP.setProduit(savedProduit);
        stockCUMP.setCoutUnitaireCUMP(prixAchat);
        stockCUMPRepository.save(stockCUMP);

        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduit(savedProduit);
        mouvement.setTypeMouvement(TypeMouvement.ENTREE);
        mouvement.setQuantite(savedProduit.getStockActuel());
        mouvement.setCoutUnitaire(prixAchat);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementStockRepository.save(mouvement);

        return produitMapper.toDTO(savedProduit);
    }

    @Override
    @Transactional
    public ProduitDTO update(Long id, ProduitDTO dto) {
        return produitRepository.findById(id)
                .map(existing -> {
                    existing.setNom(dto.getNom());
                    existing.setDescription(dto.getDescription());
                    existing.setPrixUnitaire(dto.getPrixUnitaire());
                    Produit saved = produitRepository.save(existing);
                    return produitMapper.toDTO(saved);
                })
                .orElse(null);
    }


    @Override
    @Transactional
    public ProduitDTO ajusterStock(Long produitId, int quantite, double prixAchat, TypeMouvement type) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (type == TypeMouvement.ENTREE) {
            produit.setStockActuel(produit.getStockActuel() + quantite);

            StockCUMP stockCUMP = stockCUMPRepository.findByProduitId(produitId)
                    .orElseGet(() -> {
                        StockCUMP s = new StockCUMP();
                        s.setProduit(produit);
                        s.setCoutUnitaireCUMP(produit.getPrixUnitaire());
                        return s;
                    });

            List<MouvementStock> mouvements = mouvementStockRepository.findByProduitIdAndTypeMouvement(
                    produitId, TypeMouvement.ENTREE);

            double sommeValeur = mouvements.stream()
                    .mapToDouble(m -> m.getQuantite() * m.getCoutUnitaire())
                    .sum();
            int sommeQuantite = mouvements.stream()
                    .mapToInt(MouvementStock::getQuantite)
                    .sum();

            double cump = (sommeValeur + quantite * prixAchat) / (sommeQuantite + quantite);

            stockCUMP.setCoutUnitaireCUMP(cump);
            stockCUMPRepository.save(stockCUMP);

        }

        produitRepository.save(produit);

        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduit(produit);
        mouvement.setTypeMouvement(type);
        mouvement.setQuantite(quantite);
        mouvement.setCoutUnitaire(prixAchat);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementStockRepository.save(mouvement);

        return produitMapper.toDTO(produit);
    }
}
