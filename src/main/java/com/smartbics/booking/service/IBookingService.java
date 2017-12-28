package com.smartbics.booking.service;

import com.smartbics.booking.BookingStatus;
import com.smartbics.booking.model.MeetingInformationDto;
import com.smartbics.booking.beans.BookingCalendar;

import java.time.LocalDate;


public interface IBookingService {

    /**
     * Booking boardroom if it is not busy and satisfies office working hours.
     *
     * @param input - meeting data
     * @return calendar of booking
     */
    BookingCalendar book(MeetingInformationDto input);

    BookingStatus getBookingStatusByDate(LocalDate date);

}
