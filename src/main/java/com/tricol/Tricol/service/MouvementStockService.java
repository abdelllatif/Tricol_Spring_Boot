package com.tricol.Tricol.service;

import com.tricol.Tricol.dto.MouvementStockDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

public interface MouvementStockService {

    List<MouvementStockDTO> getAllMouvements();

    List<MouvementStockDTO> getMouvementsByProduit(Long produitId);

    Page<MouvementStockDTO> getMouvementsPaged(String produitName, String date, Pageable pageable);

    Page<MouvementStockDTO> getMouvementsByProduitPaged(Long produitId, Pageable pageable);

    void exportToExcel(HttpServletResponse response) throws IOException;
}
