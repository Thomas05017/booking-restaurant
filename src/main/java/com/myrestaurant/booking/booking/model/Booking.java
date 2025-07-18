package com.myrestaurant.booking.booking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myrestaurant.booking.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private int numberOfGuests;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Booking() {}

    public Booking(LocalDate date, LocalTime time, int numberOfGuests, User user) {
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.user = user;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
