package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "studios", schema = "public", catalog = "kino")
public class StudiosEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "studio_id")
    private int studioId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "country")
    private String country;
    @OneToMany(mappedBy = "studiosByStudioId")
    private Collection<MoviesEntity> moviesByStudioId;

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudiosEntity that = (StudiosEntity) o;

        if (studioId != that.studioId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studioId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    public Collection<MoviesEntity> getMoviesByStudioId() {
        return moviesByStudioId;
    }

    public void setMoviesByStudioId(Collection<MoviesEntity> moviesByStudioId) {
        this.moviesByStudioId = moviesByStudioId;
    }
}
