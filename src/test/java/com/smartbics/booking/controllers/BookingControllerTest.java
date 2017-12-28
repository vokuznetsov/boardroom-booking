package com.smartbics.booking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbics.booking.beans.BookingCalendar;
import com.smartbics.booking.config.WebTestConfig;
import com.smartbics.booking.dto.BookingInformationTestDto;
import com.smartbics.booking.dto.MeetingInformationTestDto;
import com.smartbics.booking.dto.WorkingHoursTestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@EnableAutoConfiguration
@SpringBootTest(classes = {
        WebTestConfig.class
})
@ComponentScan(value = "com.smartbics.booking")
@RunWith(SpringRunner.class)
public class BookingControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter WORKING_HOURS_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter SUBMISSION_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter MEETING_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingCalendar bookingCalendar;



    @Test
    public void bookBoardroom() throws Exception {
        String workingStartTime = "0900";
        String workingEndTime = "1730";
        String meetingTime = "2011-03-21 09:00";

        WorkingHoursTestDto workingHours = new WorkingHoursTestDto(workingStartTime, workingEndTime);
        BookingInformationTestDto bookingInformation = new BookingInformationTestDto("2011-03-17 10:17:06", "EMP001", meetingTime, 2);


        MeetingInformationTestDto meeting = new MeetingInformationTestDto();
        meeting.setWorkingHours(workingHours);
        meeting.setBookingInformation(bookingInformation);

        mockMvc.perform(post("/booking").content(json(meeting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status[0].day").value("2011-03-21"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0]").exists())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("09:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].end").value("11:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].employeeId").value("EMP001"));

    }

    /**
     * Serialize an arbitrary object into a JSON string.
     * @param object source object
     * @return JSON string representing the object
     * @throws JsonProcessingException when something went wrong
     */
    private String json(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}