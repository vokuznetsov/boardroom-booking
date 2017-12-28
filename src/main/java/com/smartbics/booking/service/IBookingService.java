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

    /**
     * Return reservation time of boardroom for specific date
     *
     * @param date
     * @return
     */
    BookingStatus getBookingStatusByDate(LocalDate date);

}
