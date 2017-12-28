package com.smartbics.booking.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OutputFormatDto {

    @JsonProperty("booking-status")
    private List<BookingStatusDto> bookingStatuses = new ArrayList<>();

    public OutputFormatDto() {
    }

    public List<BookingStatusDto> getBookingStatuses() {
        return bookingStatuses;
    }

    public BookingStatusDto getBookingStatus(LocalDate date) {
        return bookingStatuses.stream()
                .filter(status -> status.getDay().equals(date))
                .findFirst()
                .orElse(null);
    }

    public void setBookingStatuses(List<BookingStatusDto> bookingStatuses) {
        this.bookingStatuses = bookingStatuses;
    }
}
