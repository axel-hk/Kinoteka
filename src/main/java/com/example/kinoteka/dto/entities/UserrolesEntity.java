package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "userroles", schema = "public", catalog = "kino")
public class UserrolesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "rolename")
    private String rolename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserrolesEntity that = (UserrolesEntity) o;

        if (id != that.id) return false;
        if (rolename != null ? !rolename.equals(that.rolename) : that.rolename != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
        return result;
    }
}
