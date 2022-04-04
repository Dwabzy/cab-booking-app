package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtRequest {
    String email;
    String password;
}