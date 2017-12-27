package com.smartbics.booking.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class OutputFormatDto {

    @JsonProperty("booking-status")
    private List<BookingStatusDto> bookingStatus = new ArrayList<>();

    public OutputFormatDto() {
    }

    public List<BookingStatusDto> getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(List<BookingStatusDto> bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
