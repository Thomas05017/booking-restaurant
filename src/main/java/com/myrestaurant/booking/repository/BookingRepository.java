package com.myrestaurant.booking.repository;

import com.myrestaurant.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByDate(LocalDate date);
    
    List<Booking> findByDateAndTime(LocalDate date, LocalTime time);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.date DESC, b.time DESC")
    List<Booking> findByUserIdOrderByDateTimeDesc(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.date >= :startDate AND b.date <= :endDate ORDER BY b.date, b.time")
    List<Booking> findByDateBetweenOrderByDateTime(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}