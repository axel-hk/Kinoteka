package com.example.kinoteka.dao.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "userroles", schema = "public", catalog = "kino")
public class UserrolesEntity {
    @Id
    @Basic
    @Column(name = "rolename")
    private String rolename;

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

        if (rolename != null ? !rolename.equals(that.rolename) : that.rolename != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
        return result;
    }
}
