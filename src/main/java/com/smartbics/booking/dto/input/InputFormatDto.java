package com.smartbics.booking.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

public class InputFormatDto {

    @JsonProperty("working-hours")
    private WorkingHoursDto workingHours;

    @JsonProperty("booking-information")
    @Valid
    private BookingInformationDto bookingInformation;

    public InputFormatDto() {
    }

    public InputFormatDto(WorkingHoursDto workingHours) {
        this.workingHours = workingHours;
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