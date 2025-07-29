package com.myrestaurant.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.booking.dto.BookingResponseDTO;
import com.myrestaurant.booking.service.BookingService;
import com.myrestaurant.exception.GlobalExceptionHandler;
import com.myrestaurant.exception.InvalidBookingException;
import com.myrestaurant.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;
    private BookingService bookingService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookingService = mock(BookingService.class);
        BookingController bookingController = new BookingController(bookingService);
        
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Dovrebbe ottenere tutte le prenotazioni")
    void shouldGetAllBookings() throws Exception {
        // Given
        BookingResponseDTO booking = new BookingResponseDTO(
            1L, LocalDate.now().plusDays(1), LocalTime.of(20, 0), 4, "testuser", "CONFIRMED");
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        // When & Then
        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].numberOfGuests").value(4));

        verify(bookingService).getAllBookings();
    }

    @Test
    @DisplayName("Dovrebbe creare una prenotazione")
    void shouldCreateBooking() throws Exception {
        // Given
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(1L);
        bookingDTO.setDate(LocalDate.now().plusDays(1));
        bookingDTO.setTime(LocalTime.of(20, 0));
        bookingDTO.setNumberOfGuests(4);

        BookingResponseDTO responseDTO = new BookingResponseDTO(
            1L, bookingDTO.getDate(), bookingDTO.getTime(), 
            bookingDTO.getNumberOfGuests(), "testuser", "CONFIRMED");

        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(bookingService).createBooking(any(BookingDTO.class));
    }

    @Test
    @DisplayName("Dovrebbe restituire 404 quando l'utente non esiste")
    void shouldReturn404WhenUserNotFound() throws Exception {
        // Given
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(999L);
        bookingDTO.setDate(LocalDate.now().plusDays(1));
        bookingDTO.setTime(LocalTime.of(20, 0));
        bookingDTO.setNumberOfGuests(4);

        when(bookingService.createBooking(any(BookingDTO.class)))
            .thenThrow(new UserNotFoundException("Utente non trovato"));

        // When & Then
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Utente non trovato"))
                .andExpect(jsonPath("$.error").value("User Not Found"));

        verify(bookingService).createBooking(any(BookingDTO.class));
    }

    @Test
    @DisplayName("Dovrebbe restituire 400 per prenotazione non valida")
    void shouldReturn400ForInvalidBooking() throws Exception {
        // Given
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(1L);
        bookingDTO.setDate(LocalDate.now().minusDays(1)); // Data passata
        bookingDTO.setTime(LocalTime.of(20, 0));
        bookingDTO.setNumberOfGuests(4);

        when(bookingService.createBooking(any(BookingDTO.class)))
            .thenThrow(new InvalidBookingException("Non è possibile prenotare per una data passata"));

        // When & Then
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Non è possibile prenotare per una data passata"))
                .andExpect(jsonPath("$.error").value("Invalid Booking"));

        verify(bookingService).createBooking(any(BookingDTO.class));
    }

    @Test
    @DisplayName("Dovrebbe eliminare una prenotazione")
    void shouldDeleteBooking() throws Exception {
        // Given
        doNothing().when(bookingService).deleteBooking(1L);

        // When & Then
        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isOk());

        verify(bookingService).deleteBooking(1L);
    }
}