package com.myrestaurant.booking.booking.repository;

import com.myrestaurant.booking.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
