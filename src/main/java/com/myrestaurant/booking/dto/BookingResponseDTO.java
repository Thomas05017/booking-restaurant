package com.myrestaurant.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingResponseDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private int numberOfGuests;
    private String username;
    private String status;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long id, LocalDate date, LocalTime time, 
                             int numberOfGuests, String username, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.username = username;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}