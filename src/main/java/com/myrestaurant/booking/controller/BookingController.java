package com.myrestaurant.booking.controller;

import com.myrestaurant.booking.model.Booking;
import com.myrestaurant.booking.repository.BookingRepository;
import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.booking.user.model.User;
import com.myrestaurant.booking.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;


    public BookingController(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @PostMapping
    public Booking createBooking(@RequestBody BookingDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDate(request.getDate());
        booking.setTime(request.getTime());
        booking.setNumberOfGuests(request.getNumberOfGuests());

        return bookingRepository.save(booking);
    }

    @PostMapping
    public BookingResponseDTO createBooking(@Valid @RequestBody BookingDTO request) {
        return bookingService.createBooking(request);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingRepository.deleteById(id);
    }
}
