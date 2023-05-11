package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "movies", schema = "public", catalog = "kino")
public class MoviesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "movie_id")
    private int movieId;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "studio_id")
    @NotNull
    private int studioId;
    @Basic
    @Column(name = "year")
    private int year;
    @Basic
    @Column(name = "country")
    private String country;
    @Basic
    @Column(name = "duration")
    private int duration;
    @Basic
    @Column(name = "genre_id")
    @NotNull
    private int genreId;
    @Basic
    @Column(name = "rating")
    private int rating;
    @ManyToOne
    @JoinColumn(name = "studio_id", referencedColumnName = "studio_id", insertable=false, updatable=false)
    private StudiosEntity studiosByStudioId;
    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id", insertable=false, updatable=false)
    private GenresEntity genresByGenreId;
    @OneToMany(mappedBy = "moviesByMovieId", fetch = FetchType.EAGER)
    private Collection<ParticipantsEntity> participantsByMovieId;
    @OneToMany(mappedBy = "moviesByMovieId", fetch = FetchType.EAGER)
    private Collection<SalesEntity> salesByMovieId;
    @OneToMany(mappedBy = "moviesByMovieId", fetch = FetchType.EAGER)
    private Collection<SessionsEntity> sessionsByMovieId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviesEntity that = (MoviesEntity) o;
        return movieId == that.movieId && studioId == that.studioId && year == that.year && duration == that.duration && genreId == that.genreId && rating == that.rating && Objects.equals(title, that.title) && Objects.equals(country, that.country) && Objects.equals(studiosByStudioId, that.studiosByStudioId) && Objects.equals(genresByGenreId, that.genresByGenreId) && Objects.equals(participantsByMovieId, that.participantsByMovieId) && Objects.equals(salesByMovieId, that.salesByMovieId) && Objects.equals(sessionsByMovieId, that.sessionsByMovieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, title, studioId, year, country, duration, genreId, rating, studiosByStudioId, genresByGenreId, participantsByMovieId, salesByMovieId, sessionsByMovieId);
    }

    public StudiosEntity getStudiosByStudioId() {
        return studiosByStudioId;
    }

    public void setStudiosByStudioId(StudiosEntity studiosByStudioId) {
        this.studiosByStudioId = studiosByStudioId;
    }

    public GenresEntity getGenresByGenreId() {
        return genresByGenreId;
    }

    public void setGenresByGenreId(GenresEntity genresByGenreId) {
        this.genresByGenreId = genresByGenreId;
    }

    public Collection<ParticipantsEntity> getParticipantsByMovieId() {
        return participantsByMovieId;
    }

    public void setParticipantsByMovieId(Collection<ParticipantsEntity> participantsByMovieId) {
        this.participantsByMovieId = participantsByMovieId;
    }

    public Collection<SalesEntity> getSalesByMovieId() {
        return salesByMovieId;
    }

    public void setSalesByMovieId(Collection<SalesEntity> salesByMovieId) {
        this.salesByMovieId = salesByMovieId;
    }

    public Collection<SessionsEntity> getSessionsByMovieId() {
        return sessionsByMovieId;
    }

    public void setSessionsByMovieId(Collection<SessionsEntity> sessionsByMovieId) {
        this.sessionsByMovieId = sessionsByMovieId;
    }
}
