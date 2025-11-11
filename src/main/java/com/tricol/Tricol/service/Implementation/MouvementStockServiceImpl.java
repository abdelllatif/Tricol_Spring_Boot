package com.tricol.Tricol.service.Implementation;

import com.tricol.Tricol.dto.MouvementStockDTO;
import com.tricol.Tricol.mapper.MouvementStockMapper;
import com.tricol.Tricol.model.MouvementStock;
import com.tricol.Tricol.repository.MouvementStockRepository;
import com.tricol.Tricol.service.MouvementStockService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository mouvementStockRepository;

    public MouvementStockServiceImpl(MouvementStockRepository mouvementStockRepository) {
        this.mouvementStockRepository = mouvementStockRepository;
    }

    @Override
    public List<MouvementStockDTO> getAllMouvements() {
        return mouvementStockRepository.findAll()
                .stream()
                .map(MouvementStockMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementStockDTO> getMouvementsByProduit(Long produitId) {
        return mouvementStockRepository.findByProduitId(produitId)
                .stream()
                .map(MouvementStockMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MouvementStockDTO> getMouvementsPaged(String produitName, String date, Pageable pageable) {
        if (date != null && !date.isEmpty()) {
            LocalDate filterDate = LocalDate.parse(date);
            return mouvementStockRepository.findByProduitNomContainingAndDateMouvement(produitName, filterDate, pageable)
                    .map(MouvementStockMapper::toDTO);
        } else {
            return mouvementStockRepository.findByProduitNomContaining(produitName, pageable)
                    .map(MouvementStockMapper::toDTO);
        }
    }

    @Override
    public Page<MouvementStockDTO> getMouvementsByProduitPaged(Long produitId, Pageable pageable) {
        return mouvementStockRepository.findByProduitId(produitId, pageable)
                .map(MouvementStockMapper::toDTO);
    }

    @Override
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<MouvementStock> mouvements = mouvementStockRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("MouvementsStock");

            // Header
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Produit", "Type", "Quantite", "Coût unitaire", "Date", "Commande Fournisseur"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Rows
            int rowIdx = 1;
            for (MouvementStock ms : mouvements) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ms.getId());
                row.createCell(1).setCellValue(ms.getProduit().getNom());
                row.createCell(2).setCellValue(ms.getTypeMouvement().name());
                row.createCell(3).setCellValue(ms.getQuantite());
                row.createCell(4).setCellValue(ms.getCoutUnitaire() != null ? ms.getCoutUnitaire() : 0);
                row.createCell(5).setCellValue(ms.getDateMouvement() != null ? ms.getDateMouvement().format(formatter) : "");
                row.createCell(6).setCellValue(ms.getCommande() != null ? ms.getCommande().getId().toString() : "");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=mouvements-stock.xlsx");
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        }
    }
}
