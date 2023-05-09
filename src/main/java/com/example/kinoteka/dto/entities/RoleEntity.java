package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "role", schema = "public", catalog = "kino")
public class RoleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id")
    private int roleId;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "roleByRoleId")
    private Collection<ParticipantsEntity> participantsByRoleId;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

        RoleEntity that = (RoleEntity) o;

        if (roleId != that.roleId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Collection<ParticipantsEntity> getParticipantsByRoleId() {
        return participantsByRoleId;
    }

    public void setParticipantsByRoleId(Collection<ParticipantsEntity> participantsByRoleId) {
        this.participantsByRoleId = participantsByRoleId;
    }
}
