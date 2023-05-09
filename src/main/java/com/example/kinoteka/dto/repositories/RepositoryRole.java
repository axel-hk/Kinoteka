package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryRole extends JpaRepository<RoleEntity, Integer> {
}
