package com.example.kinoteka.services;

import com.example.kinoteka.dao.entities.*;
import com.example.kinoteka.dto.CurrentSession;
import com.example.kinoteka.dto.GenreStatistics;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<CurrentSession> getCurrentSessions(){
        String sqlQuery = "SELECT * FROM current_session()";

        List<Object[]> resultList = entityManager.createNativeQuery(sqlQuery).getResultList();

        List<CurrentSession> currentSessions = new ArrayList<>();

        for (Object[] row : resultList) {
            String name = (String) row[0];
            int ticketCost = (int) row[1];
            LocalDateTime startTime =  ((Timestamp) row[2]).toLocalDateTime();
            int availableSeats = (int) row[3];
            int sessionNumber = (int) row[4];
            CurrentSession currentSession = new CurrentSession(name, ticketCost, startTime, availableSeats, sessionNumber);
            currentSessions.add(currentSession);

        }


        return currentSessions;
    }

    public Integer getActorAge(Integer id){
        String sqlQuery = "SELECT * FROM public.calculate_actor_age(:param)";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("param", id);

        return (Integer) query.getSingleResult();
    }

    public String getDuration(Integer duration){
        String sqlQuery = "select * from public.format_duration(:param)";

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("param", duration);

        return (String) query.getSingleResult();
    }

    public String getSername(String fullName){
        String sqlQuery = "select * from public.fio_to_familiya(:param)";

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("param", fullName);

        return (String) query.getSingleResult();
    }

    public List getMoviesWithDirectorInLeadRole(){
        String sqlQuery = "select * from public.get_movies_with_director_in_lead_role()";

        Query query = entityManager.createNativeQuery(sqlQuery);

        return query.getResultList();
    }

    public List getCinemaRevenue(LocalDate start, LocalDate end){
        String sqlQuery = "select * from public.cinema_revenue(:param1, :param2)";
        Query query =  entityManager.createNativeQuery(sqlQuery);
        query.setParameter("param1",start );
        query.setParameter("param2", end);

        return query.getResultList();
    }


    public List<MoviesEntity> getMoviesInCurrentYear() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MoviesEntity> criteriaQuery = criteriaBuilder.createQuery(MoviesEntity.class);
        Root<MoviesEntity> root = criteriaQuery.from(MoviesEntity.class);

        LocalDate currentYear = LocalDate.now();
        Predicate yearPredicate = criteriaBuilder.equal(root.get("year"), currentYear.getYear());
        Predicate countryPredicate = criteriaBuilder.equal(root.get("country"), "Finland");
        criteriaQuery.select(root).where(yearPredicate, countryPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<GenreStatistics> getGenreStatistics() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GenreStatistics> criteriaQuery = criteriaBuilder.createQuery(GenreStatistics.class);
        Root<MoviesEntity> movieRoot = criteriaQuery.from(MoviesEntity.class);
        Join<MoviesEntity, GenresEntity> genreJoin = movieRoot.join("genresByGenreId");

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<MoviesEntity> movieEntityType = metamodel.entity(MoviesEntity.class);
        EntityType<GenresEntity> genreEntityType = metamodel.entity(GenresEntity.class);

        Selection<String> genreSelection = genreJoin.get(genreEntityType.getDeclaredSingularAttribute("name", String.class));
        Selection<Long> moviesInRussiaCountSelection = criteriaBuilder.count(criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(movieRoot.get(movieEntityType.getDeclaredSingularAttribute("country", String.class)), "Finland"), 1)
                .otherwise((Long) null));
        Selection<Long> moviesInOtherCountriesCountSelection = criteriaBuilder.count(criteriaBuilder.selectCase()
                .when(criteriaBuilder.notEqual(movieRoot.get(movieEntityType.getDeclaredSingularAttribute("country", String.class)), "Finland"), 1)
                .otherwise((Long) null));

        criteriaQuery.multiselect(genreSelection, moviesInRussiaCountSelection, moviesInOtherCountriesCountSelection)
                .groupBy((Expression<?>) genreSelection);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
