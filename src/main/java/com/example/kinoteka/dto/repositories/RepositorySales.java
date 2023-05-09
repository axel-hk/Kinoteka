package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.SalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorySales extends JpaRepository<SalesEntity, Integer> {
}
