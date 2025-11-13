package com.tricol.Tricol.dto;

import lombok.Data;

@Data
public class StockCUMPDTO {
    private Long id;
    private Double coutUnitaireCUMP;

    public StockCUMPDTO(Long id, Double coutUnitaireCUMP) {
        this.id = id;
        this.coutUnitaireCUMP = coutUnitaireCUMP;
    }
}