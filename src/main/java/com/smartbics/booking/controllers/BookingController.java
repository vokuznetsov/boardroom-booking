package com.smartbics.booking.controllers;

import com.smartbics.booking.BookingStatus;
import com.smartbics.booking.model.MeetingInformationDto;
import com.smartbics.booking.beans.BookingCalendar;
import com.smartbics.booking.service.IBookingService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingCalendar bookBoardroom(@Valid @RequestBody MeetingInformationDto meeting) {
        return bookingService.book(meeting);
    }

    @GetMapping
    public BookingStatus getBookingByDate(@RequestParam(value = "date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return bookingService.getBookingStatusByDate(localDate);
    }
}
