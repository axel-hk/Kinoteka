package com.example.kinoteka.security;


import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    private UserService userService;
    @Autowired
    public SecurityService(AuthenticationContext authenticationContext, UserService userService) {
        this.authenticationContext = authenticationContext;
        this.userService = userService;
    }


    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetailsImpl.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }

    public Set<String> getAllUserRoles(){
        return userService.getAllUsersRoles(getAuthenticatedUser().getUsername());
    }
}
