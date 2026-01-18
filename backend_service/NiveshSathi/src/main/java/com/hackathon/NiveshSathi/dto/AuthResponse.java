package com.hackathon.NiveshSathi.dto;

public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String profession;

    public AuthResponse(String token, String name, String email, String profession) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.profession = profession;
    }

    // getters
}

