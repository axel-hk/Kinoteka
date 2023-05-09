package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.MoviesEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryMovies extends JpaRepository<MoviesEntity, Integer> {
}
