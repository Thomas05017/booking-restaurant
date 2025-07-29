package com.myrestaurant.booking.service;

import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.booking.dto.BookingResponseDTO;
import com.myrestaurant.booking.dto.BookingUpdateDTO;
import com.myrestaurant.booking.model.Booking;
import com.myrestaurant.booking.repository.BookingRepository;
import com.myrestaurant.booking.validator.BookingValidator;
import com.myrestaurant.user.model.User;
import com.myrestaurant.user.repository.UserRepository;
import com.myrestaurant.exception.BookingNotFoundException;
import com.myrestaurant.exception.InvalidBookingException;
import com.myrestaurant.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingValidator bookingValidator;

    public BookingService(BookingRepository bookingRepository, 
                         UserRepository userRepository, 
                         BookingValidator bookingValidator) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingValidator = bookingValidator;
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Prenotazione non trovata con ID: " + id));
        return convertToResponseDTO(booking);
    }

    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
        // Valida i dati della prenotazione
        bookingValidator.validateBooking(bookingDTO);

        // Verifica che l'utente esista
        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + bookingDTO.getUserId()));

        // Verifica disponibilità (controlla che non ci siano già prenotazioni per lo stesso orario)
        checkAvailability(bookingDTO.getDate(), bookingDTO.getTime(), bookingDTO.getNumberOfGuests());

        // Crea la prenotazione
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDate(bookingDTO.getDate());
        booking.setTime(bookingDTO.getTime());
        booking.setNumberOfGuests(bookingDTO.getNumberOfGuests());

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(savedBooking);
    }

    public BookingResponseDTO updateBooking(Long id, BookingUpdateDTO updateDTO) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Prenotazione non trovata con ID: " + id));

        // Crea un BookingDTO temporaneo per la validazione
        BookingDTO tempBookingDTO = new BookingDTO();
        tempBookingDTO.setDate(updateDTO.getDate() != null ? updateDTO.getDate() : existingBooking.getDate());
        tempBookingDTO.setTime(updateDTO.getTime() != null ? updateDTO.getTime() : existingBooking.getTime());
        tempBookingDTO.setNumberOfGuests(updateDTO.getNumberOfGuests() != null ? updateDTO.getNumberOfGuests() : existingBooking.getNumberOfGuests());
        tempBookingDTO.setUserId(existingBooking.getUser().getId());

        // Valida le modifiche
        bookingValidator.validateBooking(tempBookingDTO);

        // Verifica disponibilità solo se data/ora sono cambiate
        if (!existingBooking.getDate().equals(tempBookingDTO.getDate()) || 
            !existingBooking.getTime().equals(tempBookingDTO.getTime())) {
            checkAvailabilityForUpdate(id, tempBookingDTO.getDate(), tempBookingDTO.getTime(), tempBookingDTO.getNumberOfGuests());
        }

        // Aggiorna i campi
        if (updateDTO.getDate() != null) {
            existingBooking.setDate(updateDTO.getDate());
        }
        if (updateDTO.getTime() != null) {
            existingBooking.setTime(updateDTO.getTime());
        }
        if (updateDTO.getNumberOfGuests() != null) {
            existingBooking.setNumberOfGuests(updateDTO.getNumberOfGuests());
        }

        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToResponseDTO(updatedBooking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Prenotazione non trovata con ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + userId));

        return user.getBookings().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getBookingsByDate(LocalDate date) {
        return bookingRepository.findByDate(date).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private void checkAvailability(LocalDate date, LocalTime time, int numberOfGuests) {
        // Verifica se esiste già una prenotazione per lo stesso orario
        List<Booking> conflictingBookings = bookingRepository.findByDateAndTime(date, time);
        
        if (!conflictingBookings.isEmpty()) {
            throw new InvalidBookingException(
                "Orario non disponibile. Esiste già una prenotazione per " + date + " alle " + time
            );
        }
    }

    private void checkAvailabilityForUpdate(Long bookingId, LocalDate date, LocalTime time, int numberOfGuests) {
        List<Booking> conflictingBookings = bookingRepository.findByDateAndTime(date, time);
        
        // Rimuove la prenotazione corrente dalla lista dei conflitti
        conflictingBookings.removeIf(booking -> booking.getId().equals(bookingId));
        
        if (!conflictingBookings.isEmpty()) {
            throw new InvalidBookingException(
                "Orario non disponibile. Esiste già una prenotazione per " + date + " alle " + time
            );
        }
    }

    private BookingResponseDTO convertToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
            booking.getId(),
            booking.getDate(),
            booking.getTime(),
            booking.getNumberOfGuests(),
            booking.getUser().getUsername(),
            "CONFIRMED"
        );
    }
}