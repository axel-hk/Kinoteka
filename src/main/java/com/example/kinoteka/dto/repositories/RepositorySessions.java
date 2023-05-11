package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.ParticipantsEntity;
import com.example.kinoteka.dto.entities.SessionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorySessions extends JpaRepository<SessionsEntity, Integer> {

    List<SessionsEntity> findAllByOrderBySessionIdAsc();
}
