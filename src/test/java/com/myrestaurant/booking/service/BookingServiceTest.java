package com.myrestaurant.booking.service;

import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.booking.dto.BookingResponseDTO;
import com.myrestaurant.booking.model.Booking;
import com.myrestaurant.booking.repository.BookingRepository;
import com.myrestaurant.booking.validator.BookingValidator;
import com.myrestaurant.user.model.User;
import com.myrestaurant.user.repository.UserRepository;
import com.myrestaurant.exception.UserNotFoundException;
import com.myrestaurant.exception.InvalidBookingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingValidator bookingValidator;

    @InjectMocks
    private BookingService bookingService;

    private User testUser;
    private BookingDTO testBookingDTO;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testBookingDTO = new BookingDTO();
        testBookingDTO.setUserId(1L);
        testBookingDTO.setDate(LocalDate.now().plusDays(1));
        testBookingDTO.setTime(LocalTime.of(20, 0));
        testBookingDTO.setNumberOfGuests(4);

        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setUser(testUser);
        testBooking.setDate(testBookingDTO.getDate());
        testBooking.setTime(testBookingDTO.getTime());
        testBooking.setNumberOfGuests(testBookingDTO.getNumberOfGuests());
    }

    @Test
    @DisplayName("Dovrebbe creare una prenotazione con successo")
    void shouldCreateBookingSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.findByDateAndTime(any(), any())).thenReturn(new ArrayList<>());
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        doNothing().when(bookingValidator).validateBooking(testBookingDTO);

        // When
        BookingResponseDTO result = bookingService.createBooking(testBookingDTO);

        // Then
        assertNotNull(result);
        assertEquals(testBooking.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBookingDTO.getDate(), result.getDate());
        assertEquals(testBookingDTO.getTime(), result.getTime());
        assertEquals(testBookingDTO.getNumberOfGuests(), result.getNumberOfGuests());

        verify(bookingValidator).validateBooking(testBookingDTO);
        verify(userRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione quando l'utente non esiste")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        doNothing().when(bookingValidator).validateBooking(testBookingDTO);

        // When & Then
        assertThrows(UserNotFoundException.class, 
            () -> bookingService.createBooking(testBookingDTO));

        verify(bookingValidator).validateBooking(testBookingDTO);
        verify(userRepository).findById(1L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione quando l'orario non è disponibile")
    void shouldThrowExceptionWhenTimeSlotNotAvailable() {
        // Given
        List<Booking> conflictingBookings = List.of(testBooking);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.findByDateAndTime(any(), any())).thenReturn(conflictingBookings);
        doNothing().when(bookingValidator).validateBooking(testBookingDTO);

        // When & Then
        assertThrows(InvalidBookingException.class, 
            () -> bookingService.createBooking(testBookingDTO));

        verify(bookingValidator).validateBooking(testBookingDTO);
        verify(userRepository).findById(1L);
        verify(bookingRepository).findByDateAndTime(testBookingDTO.getDate(), testBookingDTO.getTime());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dovrebbe propagare eccezione di validazione")
    void shouldPropagateValidationException() {
        // Given
        doThrow(new InvalidBookingException("Data non valida"))
            .when(bookingValidator).validateBooking(testBookingDTO);

        // When & Then
        InvalidBookingException exception = assertThrows(InvalidBookingException.class, 
            () -> bookingService.createBooking(testBookingDTO));
        
        assertEquals("Data non valida", exception.getMessage());
        verify(bookingValidator).validateBooking(testBookingDTO);
        verify(userRepository, never()).findById(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Dovrebbe ottenere tutte le prenotazioni")
    void shouldGetAllBookings() {
        // Given
        List<Booking> bookings = List.of(testBooking);
        when(bookingRepository.findAll()).thenReturn(bookings);

        // When
        List<BookingResponseDTO> result = bookingService.getAllBookings();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBooking.getId(), result.get(0).getId());
        verify(bookingRepository).findAll();
    }

    @Test
    @DisplayName("Dovrebbe eliminare una prenotazione")
    void shouldDeleteBooking() {
        // Given
        when(bookingRepository.existsById(1L)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> bookingService.deleteBooking(1L));

        // Then
        verify(bookingRepository).existsById(1L);
        verify(bookingRepository).deleteById(1L);
    }
}