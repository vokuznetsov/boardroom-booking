package com.smartbics.booking.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartbics.booking.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Component
public class BookingCalendar {

    @JsonProperty("booking-status")
    private List<BookingStatus> bookingStatuses = new ArrayList<>();

    public BookingCalendar() {
    }

    public List<BookingStatus> getBookingStatuses() {
        return bookingStatuses;
    }

    public BookingStatus getBookingStatus(LocalDate date) {
        return bookingStatuses.stream()
                .filter(status -> status.getDay().equals(date))
                .findFirst()
                .orElse(null);
    }

    public void setBookingStatuses(List<BookingStatus> bookingStatuses) {
        this.bookingStatuses = bookingStatuses;
    }

    public BookingCalendar sort() {
        bookingStatuses.sort(Comparator.comparing(BookingStatus::getDay));
        bookingStatuses.forEach(BookingStatus::sort);
        return this;
    }
}
