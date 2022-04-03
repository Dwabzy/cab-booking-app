package com.gitlab.jspragadeesh.cabbookingapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @JsonProperty(required = true)
    private String firstName;
    private String lastName;
    private List<String> phoneNumbers;
    @Indexed(unique = true)
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Set<String> roles;
    private List<Location> locations;


    public User(String firstName, String lastName, List<String> phoneNumbers, String email, String password, Set<String> roles, List<Location> locations) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumbers = phoneNumbers;
        this.email = email;
        this.password = password;
        this.roles = roles;
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

