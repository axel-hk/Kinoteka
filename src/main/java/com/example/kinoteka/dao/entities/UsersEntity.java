package com.example.kinoteka.dao.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Table(name = "users", schema = "public", catalog = "kino")
public class UsersEntity {
    @Basic
    @Column(name = "login")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;


    public void setUserRoles(Set<UserrolesEntity> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<UserrolesEntity> getUserRoles() {
        return userRoles;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "user_role", schema = "public",
        joinColumns = { @JoinColumn(name = "user_id")},
        inverseJoinColumns =  {@JoinColumn(name = "rolename")})
    private Set<UserrolesEntity> userRoles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

}
