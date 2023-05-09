package com.example.kinoteka.security;

import com.example.kinoteka.dto.entities.UserrolesEntity;
import com.example.kinoteka.dto.repositories.RepositoryUserroles;
import com.example.kinoteka.dto.repositories.RepositoryUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final RepositoryUsers users;
    private final RepositoryUserroles roles;

    @Autowired
    public UserService(RepositoryUsers users, RepositoryUserroles roles) {
        this.users = users;
        this.roles = roles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user Not Found")));

    }
    public Set<String> getAllRoles(){
        return roles.findAll().stream().map(UserrolesEntity::getRolename).collect(Collectors.toSet());
    }
}
