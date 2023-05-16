package com.example.kinoteka.services;

import com.example.kinoteka.dao.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DtoSerivce {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    public void createEntityManager(){
        if(entityManager==null) entityManager = entityManagerFactory.createEntityManager();
    }

    public List<Object[]> getMoviesGroupedByProducer(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<MoviesEntity> moviesRoot = query.from(MoviesEntity.class);
        Join<MoviesEntity, ParticipantsEntity> participantsJoin = moviesRoot.join("participantsByMovieId");
        Join<MoviesEntity, SessionsEntity> sessionsJoin = moviesRoot.join("sessionsByMovieId");
        Join<SessionsEntity, TicketSalesEntity> ticketSalesJoin = sessionsJoin.join("ticketSalesBySessionId");
        Join<MoviesEntity, StudiosEntity> studiosJoin = moviesRoot.join("studiosByStudioId");

        query.multiselect(
                cb.sum(ticketSalesJoin.get("numTickets")),
                moviesRoot.get("title"),
                studiosJoin.get("country"),
                participantsJoin.get("fullName")
        );

        query.where(
                cb.equal(participantsJoin.get("roleId"), 1)
        );

        query.groupBy(
                moviesRoot.get("title"),
                studiosJoin.get("country"),
                participantsJoin.get("fullName"),
                moviesRoot.get("rating")
        );

        query.orderBy(
                cb.asc(moviesRoot.get("rating"))
        );

        List<Object[]> results = entityManager.createQuery(query).getResultList();

        return results;

    }




}
