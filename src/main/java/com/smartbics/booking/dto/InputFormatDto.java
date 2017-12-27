package com.smartbics.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputFormatDto {

    @JsonProperty("working-hours")
    private WorkingHoursDto workingHours;

    public InputFormatDto() {
    }

    public InputFormatDto(WorkingHoursDto workingHours) {
        this.workingHours = workingHours;
    }

    public WorkingHoursDto getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHoursDto workingHours) {
        this.workingHours = workingHours;
    }
}
