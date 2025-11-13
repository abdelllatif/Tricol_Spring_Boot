package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.CommandeFournisseurDTO;
import com.tricol.Tricol.dto.CommandeProduitDTO;
import com.tricol.Tricol.enums.StatutCommande;
import com.tricol.Tricol.enums.TypeMouvement;
import com.tricol.Tricol.mapper.CommandeFournisseurMapper;
import com.tricol.Tricol.model.*;
import com.tricol.Tricol.repository.*;
import com.tricol.Tricol.service.CommandeFournisseurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository repository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final StockCUMPRepository stockCUMPRepository;
    private final CommandeFournisseurMapper mapper;

    public CommandeFournisseurServiceImpl(
            CommandeFournisseurRepository repository,
            FournisseurRepository fournisseurRepository,
            ProduitRepository produitRepository,
            MouvementStockRepository mouvementStockRepository,
            StockCUMPRepository stockCUMPRepository,
            CommandeFournisseurMapper mapper
    ) {
        this.repository = repository;
        this.fournisseurRepository = fournisseurRepository;
        this.produitRepository = produitRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.stockCUMPRepository = stockCUMPRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CommandeFournisseurDTO createCommande(CommandeFournisseurDTO dto) {
        CommandeFournisseur entity = new CommandeFournisseur();

        entity.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new RuntimeException("Supplier not found")));

        entity.setStatut(dto.getStatut());

        List<CommandeProduit> produits = dto.getProduits().stream().map(pdto -> {
            CommandeProduit cp = new CommandeProduit();
            cp.setCommande(entity);
            cp.setProduit(produitRepository.findById(pdto.getProduitId())  // <-- use produitId directly
                    .orElseThrow(() -> new RuntimeException("Product not found")));
            cp.setQuantite(pdto.getQuantite());
            return cp;
        }).toList();

        entity.getProduits().addAll(produits);

        double total = entity.getProduits().stream()
                .mapToDouble(p -> p.getQuantite() * p.getProduit().getPrixUnitaire())
                .sum();
        entity.setMontantTotal(total);

        CommandeFournisseur saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CommandeFournisseurDTO updateCommande(Long id, CommandeFournisseurDTO dto) {
        CommandeFournisseur existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        if (existing.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new RuntimeException("Cannot modify a validated or delivered order");
        }

        // تحديث المورد
        existing.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new RuntimeException("Supplier not found")));

        // تحديث الحالة
        existing.setStatut(dto.getStatut());

        // تحديث المنتجات فقط إذا DTO فيها منتجات
        if (dto.getStatut() == StatutCommande.LIVREE) {
            for (CommandeProduit cp : existing.getProduits()) {
                Produit produit = cp.getProduit();
                int quantite = cp.getQuantite();

                // نجيب الـ CUMP الحالي فقط
                StockCUMP stockCUMP = stockCUMPRepository.findByProduitId(produit.getId())
                        .orElseThrow(() -> new RuntimeException("CUMP not found for this product"));

                double currentCUMP = stockCUMP.getCoutUnitaireCUMP();

                // نسجل mouvement من نوع SORTIE
                MouvementStock mouvement = new MouvementStock();
                mouvement.setProduit(produit);
                mouvement.setCommande(existing);
                mouvement.setTypeMouvement(TypeMouvement.SORTIE);
                mouvement.setQuantite(quantite);
                mouvement.setCoutUnitaire(currentCUMP);
                mouvement.setDateMouvement(LocalDateTime.now());
                mouvementStockRepository.save(mouvement);

                produit.setStockActuel(produit.getStockActuel() - quantite);
                produitRepository.save(produit);
            }
        }


        double total = existing.getProduits().stream()
                .mapToDouble(p -> p.getQuantite() * p.getProduit().getPrixUnitaire())
                .sum();
        existing.setMontantTotal(total);

        if (dto.getStatut() == StatutCommande.LIVREE) {
            for (CommandeProduit cp : existing.getProduits()) {
                Produit produit = cp.getProduit();
                int quantite = cp.getQuantite();

                StockCUMP stockCUMP = stockCUMPRepository.findByProduitId(produit.getId())
                        .orElseGet(() -> {
                            StockCUMP s = new StockCUMP();
                            s.setProduit(produit);
                            s.setCoutUnitaireCUMP(produit.getPrixUnitaire());
                            return s;
                        });

                double currentCUMP = stockCUMP.getCoutUnitaireCUMP();

                MouvementStock mouvement = new MouvementStock();
                mouvement.setProduit(produit);
                mouvement.setCommande(existing);
                mouvement.setTypeMouvement(TypeMouvement.SORTIE);
                mouvement.setQuantite(quantite);
                mouvement.setCoutUnitaire(currentCUMP);
                mouvement.setDateMouvement(LocalDateTime.now());

                mouvementStockRepository.save(mouvement);

                produit.setStockActuel(produit.getStockActuel() - quantite);
                produitRepository.save(produit);

                stockCUMPRepository.save(stockCUMP);
            }
        }

        return mapper.toDTO(repository.save(existing));
    }
    @Override
    public Page<CommandeFournisseurDTO> findAllPaged(String search, String statut, Pageable pageable) {
        if ((search == null || search.isEmpty()) && (statut == null || statut.isEmpty())) {
            return repository.findAll(pageable).map(mapper::toDTO);
        }

        StatutCommande statutEnum = null;
        if (statut != null && !statut.isEmpty()) {
            try {
                statutEnum = StatutCommande.valueOf(statut.toUpperCase());
            } catch (IllegalArgumentException e) {
                statutEnum = null; // ignore invalid statut
            }
        }

        // Both filters
        if (statutEnum != null && search != null && !search.isEmpty()) {
            return repository.findByStatutAndFournisseurSocieteContainingIgnoreCase(statutEnum, search, pageable)
                    .map(mapper::toDTO);
        }

        // Only statut
        if (statutEnum != null) {
            return repository.findByStatut(statutEnum, pageable).map(mapper::toDTO);
        }

        // Only search by fournisseur
        return repository.findByFournisseurSocieteContainingIgnoreCase(search, pageable)
                .map(mapper::toDTO);
    }


    @Override
    @Transactional
    public void deleteCommande(Long id) {
        CommandeFournisseur existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande not found"));
        repository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurDTO> getAllCommandes() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommandeFournisseurDTO> getCommandeById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }
}
