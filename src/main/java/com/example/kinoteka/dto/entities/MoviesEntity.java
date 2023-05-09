package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;

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
    @Column(name = "studio_id", insertable=false, updatable=false)
    private Integer studioId;
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
    @Column(name = "genre_id", insertable=false, updatable=false)
    private Integer genreId;
    @Basic
    @Column(name = "rating")
    private Integer rating;
    @ManyToOne
    @JoinColumn(name = "studio_id", referencedColumnName = "studio_id")
    private StudiosEntity studiosByStudioId;
    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    private GenresEntity genresByGenreId;
    @OneToMany(mappedBy = "moviesByMovieId")
    private Collection<ParticipantsEntity> participantsByMovieId;
    @OneToMany(mappedBy = "moviesByMovieId")
    private Collection<SalesEntity> salesByMovieId;
    @OneToMany(mappedBy = "moviesByMovieId")
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

    public Integer getStudioId() {
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

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Integer getRating() {
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

        if (movieId != that.movieId) return false;
        if (year != that.year) return false;
        if (duration != that.duration) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (studioId != null ? !studioId.equals(that.studioId) : that.studioId != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (genreId != null ? !genreId.equals(that.genreId) : that.genreId != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = movieId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (studioId != null ? studioId.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (genreId != null ? genreId.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
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
