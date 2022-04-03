package com.gitlab.jspragadeesh.cabbookingapp.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String name;
    private Address address;

    // Override contains method
    @Override
    public boolean equals(Object obj) {
        if(this.name.equals(((Location)obj).getName()) && this.name != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
