package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.UserrolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryUserroles extends JpaRepository<UserrolesEntity, Integer> {
}
