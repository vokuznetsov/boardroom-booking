package com.smartbics.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is necessary only for correction mapping in json and sending to controller.
 */
public class MeetingInformationTestDto {

    @JsonProperty("working-hours")
    private WorkingHoursTestDto workingHours;

    @JsonProperty("booking-information")
    private BookingInformationTestDto bookingInformation;

    public MeetingInformationTestDto() {
    }

    public MeetingInformationTestDto(WorkingHoursTestDto workingHours, BookingInformationTestDto bookingInformation) {
        this.workingHours = workingHours;
        this.bookingInformation = bookingInformation;
    }

    public WorkingHoursTestDto getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHoursTestDto workingHours) {
        this.workingHours = workingHours;
    }

    public BookingInformationTestDto getBookingInformation() {
        return bookingInformation;
    }

    public void setBookingInformation(BookingInformationTestDto bookingInformation) {
        this.bookingInformation = bookingInformation;
    }
}
