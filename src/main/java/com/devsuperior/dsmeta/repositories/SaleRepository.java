package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dtos.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando um DTO para o Service (Teste 001)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @Query(value = "SELECT new com.devsuperior.dsmeta.dtos.SaleMinDTO(s.id, s.amount, s.date, s.seller.name) "
                 + "FROM Sale s "
                 + "WHERE (:startDate IS NULL OR s.date >= :startDate) AND "
                 + "(:endDate IS NULL OR s.date <= :endDate) AND "
                 + "(:name IS NULL OR UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :name, '%')))")
    Page<SaleMinDTO> searchSalesPeriodByDateDTO(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("name") String name,
                                                Pageable pageable);

    // Finalidade: Procura Paginada de Vendas por período de datas, retornando uma Entity para o Service (Teste 002)
    // Objetivo: atender ao 'Relatório de vendas (1., 2.)', 'Informações Complementares' e aos requisitos '2.3' e '2.4' do desafio.
    @Query(value = "SELECT s FROM Sale s JOIN FETCH s.seller se WHERE "
                 + "(COALESCE(:sellerName, '') = '' OR UPPER(se.name) LIKE :sellerName) AND "
                 + "s.date BETWEEN :minDate AND :maxDate ORDER BY s.id",
      countQuery = "SELECT count(s) FROM Sale s JOIN s.seller se WHERE "
                 + "(COALESCE(:sellerName, '') = '' OR UPPER(se.name) LIKE :sellerName) AND "
                 + "s.date BETWEEN :minDate AND :maxDate")
    Page<Sale> searchSalesPeriodByDateEntity(@Param("minDate") LocalDate minDate,
                                             @Param("maxDate") LocalDate maxDate,
                                             @Param("sellerName") String sellerName,
                                             Pageable pageable);
}