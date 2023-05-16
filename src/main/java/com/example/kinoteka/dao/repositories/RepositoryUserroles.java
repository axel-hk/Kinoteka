package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.UserrolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryUserroles extends JpaRepository<UserrolesEntity, String> {

    List<UserrolesEntity> findAllByOrderByRolenameAsc();
}
