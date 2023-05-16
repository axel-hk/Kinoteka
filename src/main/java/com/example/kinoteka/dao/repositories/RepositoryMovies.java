package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.MoviesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryMovies extends JpaRepository<MoviesEntity, Integer> {
    List<MoviesEntity> findAllByOrderByTitleAsc();
}
