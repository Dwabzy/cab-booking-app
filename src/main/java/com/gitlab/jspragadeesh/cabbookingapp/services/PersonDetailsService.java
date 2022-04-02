package com.gitlab.jspragadeesh.cabbookingapp.services;

import com.gitlab.jspragadeesh.cabbookingapp.models.Person;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = getPerson(username);
        List<GrantedAuthority> authorities = getAuthorities(person.getRoles());
        return new User(person.getEmail(), person.getPassword(), authorities);
    }

    private Person getPerson(String email) {
        Person person = personRepository.findByEmail(email);
        if(person == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return person;
    }

    private List<GrantedAuthority> getAuthorities(Set<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
