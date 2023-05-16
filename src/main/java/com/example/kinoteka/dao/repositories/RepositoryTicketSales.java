package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.TicketSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryTicketSales extends JpaRepository<TicketSalesEntity, Integer> {

    List<TicketSalesEntity> findAllByOrderBySaleId();
}
