package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "genres", schema = "public", catalog = "kino")
public class GenresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "genre_id")
    private int genreId;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "genresByGenreId")
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
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

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
