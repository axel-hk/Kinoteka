package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryRole extends JpaRepository<RoleEntity, Integer> {

    List<RoleEntity> findAllByOrderByNameAsc();
}
