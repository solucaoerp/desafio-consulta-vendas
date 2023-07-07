package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dtos.SaleMinDTO;
import com.devsuperior.dsmeta.dtos.SellerSalesSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    @Transactional(readOnly = true)
    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<SaleMinDTO> getReport(String minDateStr, String maxDateStr, String name, Pageable pageable) {

        LocalDate minDate;
        LocalDate maxDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

        if (maxDateStr != null && !maxDateStr.isEmpty()) {
            try {
                maxDate = LocalDate.parse(maxDateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data final em formato inválido. Por favor, use o formato aaaa-mm-dd.", e);
            }
        }

        if (minDateStr != null && !minDateStr.isEmpty()) {
            try {
                minDate = LocalDate.parse(minDateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inicial em formato inválido. Por favor, use o formato aaaa-mm-dd.", e);
            }
        } else {
            minDate = maxDate.minusYears(1L);
        }

        if (name == null) {
            name = "";
        }

        return repository.getReport(minDate, maxDate, name, pageable);
    }

    @Transactional(readOnly = true)
    public List<SellerSalesSummaryDTO> getSummary(String minDate, String maxDate, String name) {

        LocalDate start, end;

        if (maxDate == null) {
            end = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        } else {
            end = LocalDate.parse(maxDate);
        }

        if (minDate == null) {
            start = end.minusYears(1L);
        } else {
            start = LocalDate.parse(minDate);
        }

        return repository.getSummary(start, end, name);
    }
}