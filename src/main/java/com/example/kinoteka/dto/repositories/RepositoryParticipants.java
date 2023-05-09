package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.ParticipantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryParticipants extends JpaRepository<ParticipantsEntity, Integer> {
}
