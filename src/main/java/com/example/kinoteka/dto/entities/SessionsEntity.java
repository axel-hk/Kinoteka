package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "sessions", schema = "public", catalog = "kino")
public class SessionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "session_id")
    private int sessionId;
    @Basic
    @Column(name = "movie_id")
    @NotNull
    private Integer movieId;
    @Basic
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Basic
    @Column(name = "seats_total")
    private int seatsTotal;
    @Basic
    @Column(name = "seats_available")
    private int seatsAvailable;
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id", insertable=false, updatable=false)
    private MoviesEntity moviesByMovieId;
    @OneToMany(mappedBy = "sessionsBySessionId")
    private Collection<TicketSalesEntity> ticketSalesBySessionId;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public  LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime( LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionsEntity that = (SessionsEntity) o;

        if (sessionId != that.sessionId) return false;
        if (seatsTotal != that.seatsTotal) return false;
        if (seatsAvailable != that.seatsAvailable) return false;
        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionId;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + seatsTotal;
        result = 31 * result + seatsAvailable;
        return result;
    }

    public MoviesEntity getMoviesByMovieId() {
        return moviesByMovieId;
    }

    public void setMoviesByMovieId(MoviesEntity moviesByMovieId) {
        this.moviesByMovieId = moviesByMovieId;
    }

    public Collection<TicketSalesEntity> getTicketSalesBySessionId() {
        return ticketSalesBySessionId;
    }

    public void setTicketSalesBySessionId(Collection<TicketSalesEntity> ticketSalesBySessionId) {
        this.ticketSalesBySessionId = ticketSalesBySessionId;
    }
}
