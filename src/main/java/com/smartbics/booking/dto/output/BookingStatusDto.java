package com.smartbics.booking.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;


public class BookingStatusDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate day;

    @JsonProperty("reservation-time")
    private List<ReservationTimeDto> reservationTime;

    public BookingStatusDto() {
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<ReservationTimeDto> getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(List<ReservationTimeDto> reservationTime) {
        this.reservationTime = reservationTime;
    }
}
