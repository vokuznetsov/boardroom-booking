package com.smartbics.booking.service;

import com.smartbics.booking.dto.input.InputFormatDto;
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
        LocalDate day = input.getBookingInformation().getMeetingTime().toLocalDate();
        BookingStatusDto bookingStatus = getBookingStatus(day);
        if (bookingStatus == null) {
            bookingStatus = new BookingStatusDto();
            bookingStatus.setDay(day);

            List<ReservationTimeDto> reservationTimeList = new ArrayList<>();
            reservationTimeList.add(createReservationTime(input));

            bookingStatus.setReservationTime(reservationTimeList);
        }
        output.getBookingStatus().add(bookingStatus);
        return output;
    }

    private BookingStatusDto getBookingStatus(LocalDate date) {
        return output.getBookingStatus().stream()
                .filter(status -> status.getDay().equals(date))
                .findFirst()
                .orElse(null);
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
}
