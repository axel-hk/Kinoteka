package com.example.kinoteka.dao.repositories;

import com.example.kinoteka.dao.entities.SessionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorySessions extends JpaRepository<SessionsEntity, Integer> {

    List<SessionsEntity> findAllByOrderBySessionIdAsc();

    Optional<SessionsEntity> findBySessionId(Integer id);
}
