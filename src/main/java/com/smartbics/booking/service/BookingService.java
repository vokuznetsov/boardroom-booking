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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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

        // if meeting outside office working hours - return
        if (!insideWorkingHours(input.getWorkingHours(), startMeetingTime, endMeetingTime)) {
            return output;
        }

        // check are there any booking on the startMeetingDate
        BookingStatus bookingStatus = output.getBookingStatus(startMeetingDay);

        if (bookingStatus == null) {
            bookingStatus = new BookingStatus();

            List<ReservationTime> reservationTimeList = new ArrayList<>();
            reservationTimeList.add(createReservationTime(input));

            bookingStatus.setDay(startMeetingDay);
            bookingStatus.setReservationTime(reservationTimeList);
            output.getBookingStatuses().add(bookingStatus);
        } else {
            List<ReservationTime> reservationTimes = bookingStatus.getReservationTime();

            // check are there any collisions in meeting time and already booking time
            boolean overlapTime = reservationTimes.stream()
                    .anyMatch(time -> isTimeOverlap(time, startMeetingTime, endMeetingTime));

            if (!overlapTime) {
                reservationTimes.add(createReservationTime(input));
            } else {
                // find all collisions where submission times are later than meeting submission time.
                List<ReservationTime> collisionTime = reservationTimes.stream()
                        .filter(time -> isTimeOverlap(time, startMeetingTime, endMeetingTime))
                        .sorted(Comparator.comparing(ReservationTime::getSubmissionTime))
                        .filter(time -> time.getSubmissionTime().isAfter(input.getBookingInformation().getSubmissionTime()))
                        .collect(Collectors.toList());

                // if not empty remove all collisions and create early booking time
                if (!collisionTime.isEmpty()) {
                    reservationTimes.removeAll(collisionTime);
                    reservationTimes.add(createReservationTime(input));
                }
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
        reservationTime.setSubmissionTime(input.getBookingInformation().getSubmissionTime());

        LocalTime meetingTime = input.getBookingInformation().getMeetingTime().toLocalTime();
        int duration = input.getBookingInformation().getDuration();

        reservationTime.setStart(meetingTime);
        reservationTime.setEnd(meetingTime.plusHours(duration));
        return reservationTime;
    }

    /**
     * Check if desired time is available for booking
     *
     * @param time             - already booking time
     * @param startMeetingTime - desired start meeting time
     * @param endMeetingTime   - desired end meeting time
     * @return true: boardroom is busy, false: if boardroom is free/
     */
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

    /**
     * Check if desired time matches working hours.
     *
     * @param workingHours     - office working hours
     * @param startMeetingTime - desired start meeting time
     * @param endMeetingTime   - desired end meeting time
     * @return true: desired booking time is inside working hours, false: desired booking time is outside working hours
     */
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
