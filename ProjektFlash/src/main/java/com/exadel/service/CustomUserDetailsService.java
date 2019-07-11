package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private InternRepository InternRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Intern intern = InternRepository.findInternByEmail(email);


        if(intern == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        else {
            GrantedAuthority authorities = getUserAuthority(intern.getRole());
            return buildUserForAuthentication(intern, authorities);
        }
    }

    private GrantedAuthority getUserAuthority(String userRole) {
        GrantedAuthority roles = new SimpleGrantedAuthority(userRole);
        return roles;
    }
    private UserDetails buildUserForAuthentication(Intern intern, GrantedAuthority authorities) {
        return new org.springframework.security.core.userdetails.User(intern.getEmail(), intern.getPassword(), Collections.singleton(authorities));
    }
}
