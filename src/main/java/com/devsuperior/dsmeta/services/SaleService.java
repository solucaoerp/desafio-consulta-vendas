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

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando um DTO para o Controller (Teste 001)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @Transactional(readOnly = true)
    public Page<SaleMinDTO> searchSalesPeriodByDateDTO(String minDateStr, String maxDateStr, String name, Pageable pageable) {
        LocalDate minDate;
        LocalDate maxDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()); // Se a data final não for informada, considerar a data atual do sistema

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
            minDate = maxDate.minusYears(1L); // Se a data inicial não for informada, considerar a data de 1 ano antes da data final
        }

        if (name == null) {
            name = ""; // Se o nome não for informado, considerar o texto vazio
        }

        return repository.searchSalesPeriodByDateDTO(minDate, maxDate, name, pageable);
    }

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando um DTO para o Controller (Teste 002)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @Transactional(readOnly = true)
    public Page<SaleMinDTO> searchSalesPeriodByDateEntity(String startDateStr, String endDateStr, String sellerName, Pageable pageable) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());// 1. Se a data final não for informada, considerar a data atual do sistema

        if (endDateStr != null && !endDateStr.isEmpty()) {
            try {
                endDate = LocalDate.parse(endDateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data final em formato inválido. Por favor, use o formato aaaa-mm-dd.", e);
            }
        }

        if (startDateStr != null && !startDateStr.isEmpty()) {
            try {
                startDate = LocalDate.parse(startDateStr);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inicial em formato inválido. Por favor, use o formato aaaa-mm-dd.", e);
            }
        } else {
            startDate = endDate.minusYears(1L); // 2. Se a data inicial não for informada, considerar a data de 1 ano antes da data final
        }

        if (sellerName == null) {
            sellerName = ""; // 3. Se o nome não for informado, considerar o texto vazio.
        } else {
            sellerName = "%" + sellerName.toUpperCase() + "%";
        }

        Page<Sale> sales = repository.searchSalesPeriodByDateEntity(startDate, endDate, sellerName, pageable);

        return sales.map(SaleMinDTO::new);
    }

    @Transactional(readOnly = true)
    public List<SellerSalesSummaryDTO> searchSellerSalesSummaryByDateAndName(String minDate, String maxDate, String name) {

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

        return repository.findAllSalesSummary(start, end, name);
    }
}