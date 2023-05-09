package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.StudiosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryStudios extends JpaRepository<StudiosEntity, Integer> {
}
