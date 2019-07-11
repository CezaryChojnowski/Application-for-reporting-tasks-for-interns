package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            List<GrantedAuthority> authorities = getUserAuthority(intern.getRole());
            return buildUserForAuthentication(intern, authorities);
        }
    }

    private List<GrantedAuthority> getUserAuthority(String userRole) {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(Arrays.toString(new String[]{userRole})));
        System.out.println(roles);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }
    private UserDetails buildUserForAuthentication(Intern intern, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(intern.getEmail(), intern.getPassword(), authorities);
    }
}
