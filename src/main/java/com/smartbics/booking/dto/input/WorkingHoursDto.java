package com.smartbics.booking.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WorkingHoursDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmm")
    private LocalTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmm")
    private LocalTime end;

    public WorkingHoursDto() {
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
