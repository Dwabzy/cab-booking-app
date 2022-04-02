package com.gitlab.jspragadeesh.cabbookingapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "person")
public class Person {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private List<String> phoneNumbers;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Set<String> roles;
    private List<Location> locations;

    public Person() {
    }

    public Person(String firstName, String lastName, List<String> phoneNumbers, String email, String password, Set<String> roles, List<Location> locations) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumbers = phoneNumbers;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", locations=" + locations +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Location getLocationByName(String name) {
        if(locations != null) {
            for (Location location : locations) {
                if (location.getName().equals(name)) {
                    return location;
                }
            }
        }
        return null;
    }

    public void addLocation(Location location) {
        if(locations == null) {
            locations = new java.util.ArrayList<>();
        }
        locations.add(location);
    }

    public void removeLocation(Location location) {
        if(locations != null) {
            locations.remove(location);
        }
    }
}

