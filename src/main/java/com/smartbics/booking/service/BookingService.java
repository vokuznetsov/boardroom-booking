package com.smartbics.booking.service;

import com.smartbics.booking.model.MeetingInformationDto;
import com.smartbics.booking.model.WorkingHoursDto;
import com.smartbics.booking.BookingStatus;
import com.smartbics.booking.beans.BookingCalendar;
import com.smartbics.booking.ReservationTime;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookingService implements IBookingService {

    private final BookingCalendar output;

    public BookingService(BookingCalendar output) {
        this.output = output;
    }


    @Override
    public BookingCalendar book(MeetingInformationDto input) {
        LocalDate startMeetingDay = input.getBookingInformation().getMeetingTime().toLocalDate();
        LocalTime startMeetingTime = input.getBookingInformation().getMeetingTime().toLocalTime();
        LocalTime endMeetingTime = startMeetingTime.plusHours(input.getBookingInformation().getDuration());

        if (!insideWorkingHours(input.getWorkingHours(), startMeetingTime, endMeetingTime)) {
            return output;
        }

        BookingStatus bookingStatus = output.getBookingStatus(startMeetingDay);
        if (bookingStatus == null) {
            bookingStatus = new BookingStatus();
            bookingStatus.setDay(startMeetingDay);

            List<ReservationTime> reservationTimeList = new ArrayList<>();
            reservationTimeList.add(createReservationTime(input));

            bookingStatus.setReservationTime(reservationTimeList);
            output.getBookingStatuses().add(bookingStatus);
        } else {
            List<ReservationTime> reservationTimes = bookingStatus.getReservationTime();

            boolean overlapTime = reservationTimes.stream()
                    .anyMatch(time -> isTimeOverlap(time, startMeetingTime, endMeetingTime));

            if (!overlapTime) {
                reservationTimes.add(createReservationTime(input));
            }
        }

        return output.sort();
    }

    @Override
    public BookingStatus getBookingStatusByDate(LocalDate date) {
        BookingStatus bookingStatus = output.getBookingStatus(date);
        return bookingStatus == null ? null : bookingStatus.sort();
    }

    private ReservationTime createReservationTime(MeetingInformationDto input) {
        ReservationTime reservationTime = new ReservationTime();
        reservationTime.setEmployeeId(input.getBookingInformation().getEmployeeId());

        LocalTime meetingTime = input.getBookingInformation().getMeetingTime().toLocalTime();
        int duration = input.getBookingInformation().getDuration();
        reservationTime.setStart(meetingTime);
        reservationTime.setEnd(meetingTime.plusHours(duration));
        return reservationTime;
    }

    private boolean isTimeOverlap(ReservationTime time, LocalTime startMeetingTime, LocalTime endMeetingTime) {
        // ** - startMeetingTime/endMeetingTime
        // && - already booking time (start/end)
        // ---- - time line

        // FIRST CASE:
        // ------&&----**----&&----**------
        boolean startOverlap = (time.getStart().equals(startMeetingTime) ||
                time.getStart().isBefore(startMeetingTime)) &&
                time.getEnd().isAfter(startMeetingTime);

        // SECOND CASE:
        // ------**----&&----**----&&------
        boolean endOverlap = time.getEnd().isBefore(endMeetingTime) &&
                time.getEnd().isAfter(endMeetingTime);

        return startOverlap || endOverlap;
    }

    private boolean insideWorkingHours(WorkingHoursDto workingHours, LocalTime startMeetingTime, LocalTime endMeetingTime) {
        // ** - startMeetingTime/endMeetingTime
        // && - working hours time (start/end)
        // ---- - time line

        // ALLOWED CASE:
        // ------&&----**----**----&&------
        return (workingHours.getStart().equals(startMeetingTime) || workingHours.getStart().isBefore(startMeetingTime)) &&
                (workingHours.getEnd().equals(endMeetingTime) || workingHours.getEnd().isAfter(endMeetingTime));

    }
}
