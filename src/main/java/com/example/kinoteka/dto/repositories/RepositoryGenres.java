package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.GenresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryGenres extends JpaRepository<GenresEntity, Integer> {
    Optional<GenresEntity> findAllByName(String name);

    List<GenresEntity> findAllByOrderByNameAsc();
}
