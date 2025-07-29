package com.myrestaurant.booking.validator;

import com.myrestaurant.booking.dto.BookingDTO;
import com.myrestaurant.exception.InvalidBookingException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Component
public class BookingValidator {

    // Orari di apertura del ristorante
    private static final LocalTime OPENING_TIME = LocalTime.of(12, 0); // 12:00
    private static final LocalTime CLOSING_TIME = LocalTime.of(23, 0); // 23:00
    private static final LocalTime LUNCH_END = LocalTime.of(15, 0);    // 15:00
    private static final LocalTime DINNER_START = LocalTime.of(19, 0); // 19:00

    // Configurazioni
    private static final int MAX_GUESTS_PER_BOOKING = 20;
    private static final int MIN_GUESTS_PER_BOOKING = 1;
    private static final int MAX_DAYS_IN_ADVANCE = 60;

    public void validateBooking(BookingDTO bookingDTO) {
        validateDate(bookingDTO.getDate());
        validateTime(bookingDTO.getTime(), bookingDTO.getDate());
        validateNumberOfGuests(bookingDTO.getNumberOfGuests());
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidBookingException("La data è obbligatoria");
        }

        LocalDate today = LocalDate.now();
        
        // Non si può prenotare nel passato
        if (date.isBefore(today)) {
            throw new InvalidBookingException("Non è possibile prenotare per una data passata");
        }

        // Non si può prenotare troppo in anticipo
        if (date.isAfter(today.plusDays(MAX_DAYS_IN_ADVANCE))) {
            throw new InvalidBookingException("Non è possibile prenotare oltre " + MAX_DAYS_IN_ADVANCE + " giorni");
        }

        // Il ristorante è chiuso la domenica
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidBookingException("Il ristorante è chiuso la domenica");
        }
    }

    private void validateTime(LocalTime time, LocalDate date) {
        if (time == null) {
            throw new InvalidBookingException("L'orario è obbligatorio");
        }

        // Verifica orari di apertura
        if (time.isBefore(OPENING_TIME) || time.isAfter(CLOSING_TIME)) {
            throw new InvalidBookingException(
                "Il ristorante è aperto dalle " + OPENING_TIME + " alle " + CLOSING_TIME
            );
        }

        // Verifica pausa pomeridiana (chiuso dalle 15:00 alle 19:00)
        if (time.isAfter(LUNCH_END) && time.isBefore(DINNER_START)) {
            throw new InvalidBookingException(
                "Il ristorante è chiuso dalle " + LUNCH_END + " alle " + DINNER_START
            );
        }

        // Se la prenotazione è per oggi, verifica che non sia già passata
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now().plusHours(1))) {
            throw new InvalidBookingException(
                "È necessario prenotare con almeno 1 ora di anticipo"
            );
        }

        // Verifica che l'orario sia a intervalli di 30 minuti
        if (time.getMinute() != 0 && time.getMinute() != 30) {
            throw new InvalidBookingException(
                "Le prenotazioni sono accettate solo ogni 30 minuti (esempio: 12:00, 12:30, 13:00, ecc.)"
            );
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_GUESTS_PER_BOOKING) {
            throw new InvalidBookingException(
                "Il numero minimo di ospiti è " + MIN_GUESTS_PER_BOOKING
            );
        }

        if (numberOfGuests > MAX_GUESTS_PER_BOOKING) {
            throw new InvalidBookingException(
                "Il numero massimo di ospiti per prenotazione è " + MAX_GUESTS_PER_BOOKING
            );
        }
    }

    // Metodi per ottenere le configurazioni
    public static LocalTime getOpeningTime() { return OPENING_TIME; }
    public static LocalTime getClosingTime() { return CLOSING_TIME; }
    public static int getMaxGuestsPerBooking() { return MAX_GUESTS_PER_BOOKING; }
    public static int getMaxDaysInAdvance() { return MAX_DAYS_IN_ADVANCE; }
}