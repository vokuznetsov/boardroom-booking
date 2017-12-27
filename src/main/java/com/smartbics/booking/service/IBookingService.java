package com.smartbics.booking.service;

import com.smartbics.booking.dto.input.InputFormatDto;
import com.smartbics.booking.dto.output.OutputFormatDto;


public interface IBookingService {

    OutputFormatDto book(InputFormatDto input);

}
