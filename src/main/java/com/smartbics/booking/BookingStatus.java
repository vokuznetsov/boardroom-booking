package com.smartbics.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


public class BookingStatus {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate day;

    @JsonProperty("reservation-time")
    private List<ReservationTime> reservationTime;

    public BookingStatus() {
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<ReservationTime> getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(List<ReservationTime> reservationTime) {
        this.reservationTime = reservationTime;
    }

    public BookingStatus sort() {
        reservationTime.sort(Comparator.comparing(ReservationTime::getStart));
        return this;
    }
}
