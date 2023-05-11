package com.example.kinoteka.dto.repositories;

import com.example.kinoteka.dto.entities.MoviesEntity;
import com.example.kinoteka.dto.entities.ParticipantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryParticipants extends JpaRepository<ParticipantsEntity, Integer> {

    List<ParticipantsEntity> findAllByOrderByFullNameAsc();
}
