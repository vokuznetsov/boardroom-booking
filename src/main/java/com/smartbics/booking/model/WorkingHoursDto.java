package com.smartbics.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

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
