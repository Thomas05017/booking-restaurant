package com.myrestaurant.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class UserRegistrationDTO {

    @NotBlank(message = "Username è obbligatorio")
    @Size(min = 3, max = 20, message = "Username deve essere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username può contenere solo lettere, numeri e underscore")
    private String username;

    @NotBlank(message = "Password è obbligatoria")
    @Size(min = 6, max = 100, message = "Password deve essere tra 6 e 100 caratteri")
    private String password;

    @NotBlank(message = "Conferma password è obbligatoria")
    private String confirmPassword;

    public UserRegistrationDTO() {}

    public UserRegistrationDTO(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}