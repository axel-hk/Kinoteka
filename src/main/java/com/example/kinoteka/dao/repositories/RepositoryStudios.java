package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.StudiosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryStudios extends JpaRepository<StudiosEntity, Integer> {

    List<StudiosEntity> findAllByOrderByNameAsc();
}
