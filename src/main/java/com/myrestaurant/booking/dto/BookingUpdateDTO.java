package com.myrestaurant.booking.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingUpdateDTO {
    
    @FutureOrPresent(message = "La data deve essere presente o futura")
    private LocalDate date;

    @NotNull(message = "L'orario è obbligatorio")
    private LocalTime time;

    @Min(value = 1, message = "Il numero di ospiti deve essere almeno 1")
    @Max(value = 20, message = "Il numero massimo di ospiti è 20")
    private Integer numberOfGuests;

    public BookingUpdateDTO() {}

    public BookingUpdateDTO(LocalDate date, LocalTime time, Integer numberOfGuests) {
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
    }

    // Getters e Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public Integer getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(Integer numberOfGuests) { this.numberOfGuests = numberOfGuests; }
}