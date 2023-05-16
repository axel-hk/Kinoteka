package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryUsers extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsername(String username);

    List<UsersEntity> findAllByOrderByUsername();
}
