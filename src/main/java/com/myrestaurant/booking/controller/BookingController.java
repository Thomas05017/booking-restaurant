package com.myrestaurant.booking.controller;

import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.booking.dto.BookingResponseDTO;
import com.myrestaurant.booking.dto.BookingUpdateDTO;
import com.myrestaurant.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public BookingResponseDTO createBooking(@Valid @RequestBody BookingDTO request) {
        return bookingService.createBooking(request);
    }

    @PutMapping("/{id}")
    public BookingResponseDTO updateBooking(@PathVariable Long id, @Valid @RequestBody BookingUpdateDTO updateDTO) {
        return bookingService.updateBooking(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<BookingResponseDTO> getBookingsByUser(@PathVariable Long userId) {
        return bookingService.getBookingsByUser(userId);
    }

    @GetMapping("/date/{date}")
    public List<BookingResponseDTO> getBookingsByDate(@PathVariable LocalDate date) {
        return bookingService.getBookingsByDate(date);
    }
}