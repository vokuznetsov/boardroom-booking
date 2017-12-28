package com.smartbics.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

public class MeetingInformationDto {

    @JsonProperty("working-hours")
    private WorkingHoursDto workingHours;

    @JsonProperty("booking-information")
    @Valid
    private BookingInformationDto bookingInformation;

    public MeetingInformationDto() {
    }

    public WorkingHoursDto getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHoursDto workingHours) {
        this.workingHours = workingHours;
    }

    public BookingInformationDto getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(BookingInformationDto bookingInformation) {
        this.bookingInformation = bookingInformation;
    }
}
