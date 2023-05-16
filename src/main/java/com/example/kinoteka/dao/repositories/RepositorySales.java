package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.SalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorySales extends JpaRepository<SalesEntity, Integer> {
}
