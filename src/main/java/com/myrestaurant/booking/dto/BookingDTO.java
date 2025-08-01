package com.myrestaurant.booking.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDTO {
    private Long id;
    
    @NotNull(message = "La data è obbligatoria")
    @FutureOrPresent(message = "La data deve essere presente o futura")
    private LocalDate date;

    @NotNull(message = "L'orario è obbligatorio")
    private LocalTime time;

    @Min(value = 1, message = "Il numero di ospiti deve essere almeno 1")
    @Max(value = 20, message = "Il numero massimo di ospiti è 20")
    private int numberOfGuests;

    @NotNull(message = "L'ID utente è obbligatorio")
    private Long userId;

    // Costruttori
    public BookingDTO() {}

    public BookingDTO(LocalDate date, LocalTime time, int numberOfGuests, Long userId) {
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.userId = userId;
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

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
