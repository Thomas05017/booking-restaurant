package com.myrestaurant.booking.dto;

import jakarta.validation.constraints.NotBlank;

public class UserCreateDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UserCreateDTO() {}

    public UserCreateDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}