package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dtos.SaleMinDTO;
import com.devsuperior.dsmeta.dtos.SellerSalesSummaryDTO;
import com.devsuperior.dsmeta.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

    @Autowired
    private SaleService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
        SaleMinDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando um DTO para o Cliente (Teste 001)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @GetMapping(value = "/report")
    public ResponseEntity<Page<SaleMinDTO>> searchSalesPeriodByDateDTO(
            @RequestParam(value = "minDate", required = false) String minDate,
            @RequestParam(value = "maxDate", required = false) String maxDate,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable) {

        Page<SaleMinDTO> page = service.searchSalesPeriodByDateDTO(minDate, maxDate, name, pageable);

        return ResponseEntity.ok(page);
    }

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando um DTO para o Cliente (Teste 002)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @GetMapping(value = "/report-entity")
    public ResponseEntity<Page<SaleMinDTO>> searchSalesPeriodByDateEntity(
            @RequestParam(value = "minDate", required = false) String minDate,
            @RequestParam(value = "maxDate", required = false) String maxDate,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable) {

        Page<SaleMinDTO> page = service.searchSalesPeriodByDateEntity(minDate, maxDate, name, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/summary")
    public ResponseEntity<List<SellerSalesSummaryDTO>> getSalesSummary(
            @RequestParam(value = "minDate", required = false) String minDate,
            @RequestParam(value = "maxDate", required = false) String maxDate,
            @RequestParam(value = "name", defaultValue = "") String name) {

        List<SellerSalesSummaryDTO> summary = service.searchSellerSalesSummaryByDateAndName(minDate, maxDate, name);
        return ResponseEntity.ok(summary);
    }
}