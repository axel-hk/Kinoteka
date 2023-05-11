package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "genres", schema = "public", catalog = "kino")
public class GenresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", columnDefinition = "serial")
    private int genreId;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "genresByGenreId", fetch = FetchType.EAGER)
    private Collection<MoviesEntity> moviesByGenreId;

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenresEntity that = (GenresEntity) o;

        if (genreId != that.genreId) return false;
        if (!Objects.equals(name, that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = genreId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Collection<MoviesEntity> getMoviesByGenreId() {
        return moviesByGenreId;
    }

    public void setMoviesByGenreId(Collection<MoviesEntity> moviesByGenreId) {
        this.moviesByGenreId = moviesByGenreId;
    }
}
