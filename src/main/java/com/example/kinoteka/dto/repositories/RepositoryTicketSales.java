package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.TicketSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTicketSales extends JpaRepository<TicketSalesEntity, Integer> {
}
