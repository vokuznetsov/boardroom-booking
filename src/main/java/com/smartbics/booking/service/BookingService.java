package com.smartbics.booking.service;

import com.smartbics.booking.dto.input.InputFormatDto;
import com.smartbics.booking.dto.input.WorkingHoursDto;
import com.smartbics.booking.dto.output.BookingStatusDto;
import com.smartbics.booking.dto.output.OutputFormatDto;
import com.smartbics.booking.dto.output.ReservationTimeDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class BookingService implements IBookingService {

    private final OutputFormatDto output;

    public BookingService(OutputFormatDto output) {
        this.output = output;
    }


    @Override
    public OutputFormatDto book(InputFormatDto input) {
        LocalDate startMeetingDay = input.getBookingInformation().getMeetingTime().toLocalDate();
        LocalTime startMeetingTime = input.getBookingInformation().getMeetingTime().toLocalTime();
        LocalTime endMeetingTime = startMeetingTime.plusHours(input.getBookingInformation().getDuration());

        if (!insideWorkingHours(input.getWorkingHours(), startMeetingTime, endMeetingTime)) {
            return output;
        }

        BookingStatusDto bookingStatus = output.getBookingStatus(startMeetingDay);
        if (bookingStatus == null) {
            bookingStatus = new BookingStatusDto();
            bookingStatus.setDay(startMeetingDay);

            List<ReservationTimeDto> reservationTimeList = new ArrayList<>();
            reservationTimeList.add(createReservationTime(input));

            bookingStatus.setReservationTime(reservationTimeList);
            output.getBookingStatuses().add(bookingStatus);
        } else {
            List<ReservationTimeDto> reservationTimes = bookingStatus.getReservationTime();

            boolean overlapTime = reservationTimes.stream()
                    .anyMatch(time -> isTimeOverlap(time, startMeetingTime, endMeetingTime));

            if (!overlapTime) {
                reservationTimes.add(createReservationTime(input));
            }
        }

        return output;
    }

    private ReservationTimeDto createReservationTime(InputFormatDto input) {
        ReservationTimeDto reservationTime = new ReservationTimeDto();
        reservationTime.setEmployeeId(input.getBookingInformation().getEmployeeId());

        LocalTime meetingTime = input.getBookingInformation().getMeetingTime().toLocalTime();
        int duration = input.getBookingInformation().getDuration();
        reservationTime.setStart(meetingTime);
        reservationTime.setEnd(meetingTime.plusHours(duration));
        return reservationTime;
    }

    private boolean isTimeOverlap(ReservationTimeDto time, LocalTime startMeetingTime, LocalTime endMeetingTime) {
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
