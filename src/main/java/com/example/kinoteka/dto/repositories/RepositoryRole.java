package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.ParticipantsEntity;
import com.example.kinoteka.dto.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryRole extends JpaRepository<RoleEntity, Integer> {

    List<RoleEntity> findAllByOrderByNameAsc();
}
