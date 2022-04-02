package com.gitlab.jspragadeesh.cabbookingapp.models;

public class JwtResponse {
    private String token;

    public JwtResponse(){

    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
