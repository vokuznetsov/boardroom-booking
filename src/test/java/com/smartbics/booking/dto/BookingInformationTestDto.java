package com.smartbics.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingInformationTestDto {

    @JsonProperty("submission-time")
    private String submissionTime;

    private String employeeId;

    @JsonProperty("meeting-time")
    private String meetingTime;

    private Integer duration;

    public BookingInformationTestDto() {
    }

    public BookingInformationTestDto(String submissionTime, String employeeId, String meetingTime, Integer duration) {
        this.submissionTime = submissionTime;
        this.employeeId = employeeId;
        this.meetingTime = meetingTime;
        this.duration = duration;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
