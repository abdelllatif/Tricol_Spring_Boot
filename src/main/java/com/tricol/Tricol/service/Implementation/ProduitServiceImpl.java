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
        // 1️⃣ Create product
        Produit produit = produitMapper.toEntity(dto);
        Produit savedProduit = produitRepository.save(produit);

        // 2️⃣ Create initial CUMP
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
    public ProduitDTO ajusterStock(Long produitId, int quantite, TypeMouvement type) {
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

            // new CUMP = (ancienneCUMP * ancienStock + prixEntrant * quantiteEntrante) / nouveauStock
            double ancienCUMP = stockCUMP.getCoutUnitaireCUMP();
            int ancienStock = produit.getStockActuel() - quantite;
            double nouveauCUMP = (ancienCUMP * ancienStock + produit.getPrixUnitaire() * quantite)
                    / produit.getStockActuel();
            stockCUMP.setCoutUnitaireCUMP(nouveauCUMP);
            stockCUMPRepository.save(stockCUMP);

        } else if (type == TypeMouvement.SORTIE) {
            if (produit.getStockActuel() < quantite) {
                throw new IllegalArgumentException("Stock insuffisant !");
            }
            produit.setStockActuel(produit.getStockActuel() - quantite);
        }

        produitRepository.save(produit);
        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduit(produit);
        mouvement.setTypeMouvement(type);
        mouvement.setQuantite(quantite);
        mouvement.setCoutUnitaire(produit.getPrixUnitaire());
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementStockRepository.save(mouvement);

        return produitMapper.toDTO(produit);
    }
}
