package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private List<String> phoneNumbers;
    private String email;
    private String password;
}
