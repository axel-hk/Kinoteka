package com.example.kinoteka.dto;

import jakarta.persistence.criteria.CriteriaBuilder;

public class GenreStatistics {
    String genre;
    Long moviesInCountry;
    Long moviesOutCountry;

    public GenreStatistics(String genre, Long moviesInCountry, Long moviesOutCountry) {
        this.genre = genre;
        this.moviesInCountry = moviesInCountry;
        this.moviesOutCountry = moviesOutCountry;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getMoviesInCountry() {
        return moviesInCountry;
    }

    public void setMoviesInCountry(Long moviesInCountry) {
        this.moviesInCountry = moviesInCountry;
    }

    public Long getMoviesOutCountry() {
        return moviesOutCountry;
    }

    public void setMoviesOutCountry(Long moviesOutCountry) {
        this.moviesOutCountry = moviesOutCountry;
    }
}
