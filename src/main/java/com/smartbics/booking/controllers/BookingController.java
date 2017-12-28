package com.smartbics.booking.controllers;

import com.smartbics.booking.dto.input.InputFormatDto;
import com.smartbics.booking.dto.output.OutputFormatDto;
import com.smartbics.booking.service.IBookingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final IBookingService bookingService;
    private OutputFormatDto output;

    public BookingController(IBookingService bookingService,
                             OutputFormatDto output) {
        this.bookingService = bookingService;
        this.output = output;
    }

    @GetMapping("/test")
    public String testMethod() {
        System.out.println("Output" + output);
        return "test";
    }

    @PostMapping()
    public OutputFormatDto input(@Valid @RequestBody InputFormatDto input) {
        OutputFormatDto output = bookingService.book(input);
        return output;
    }
}
