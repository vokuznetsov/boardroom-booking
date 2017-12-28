package com.smartbics.booking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbics.booking.beans.BookingCalendar;
import com.smartbics.booking.config.WebTest;
import com.smartbics.booking.dto.BookingInformationTestDto;
import com.smartbics.booking.dto.MeetingInformationTestDto;
import com.smartbics.booking.dto.WorkingHoursTestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebTest
public class BookingControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String WORKING_START_TIME = "0900";
    private static final String WORKING_END_TIME = "1730";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingCalendar calendar;

    @Before
    public void before() throws Exception {
        calendar.getBookingStatuses().clear();
    }

    @Test
    public void getBookingByDateTest() throws Exception {
        String employeeId = "EMP001";
        Integer duration = 2;

        String meetingTime1 = "2011-03-19 09:00";
        String submissionTime1 = "2011-03-17 09:17:06";

        String meetingTime2 = "2011-03-19 11:00";
        String submissionTime2 = "2011-03-17 10:33:18";

        String meetingTime3 = "2011-03-19 15:00";
        String submissionTime3 = "2011-03-17 11:33:18";

        MeetingInformationTestDto meeting1 = createMeeting(submissionTime1, employeeId, meetingTime1, duration);
        MeetingInformationTestDto meeting2 = createMeeting(submissionTime2, employeeId, meetingTime2, duration);
        MeetingInformationTestDto meeting3 = createMeeting(submissionTime3, employeeId, meetingTime3, duration);

        postRequest(meeting1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0]").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("09:00"));

        postRequest(meeting2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0]").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("09:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[1].start").value("11:00"));

        postRequest(meeting3)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status[0]").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("09:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[1].start").value("11:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[2].start").value("15:00"));

        mockMvc.perform(get("/booking").param("date", "2011-03-19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.day").value("2011-03-19"))
                .andExpect(jsonPath("$.reservation-time.length()", is(3)))
                .andExpect(jsonPath("$.reservation-time[0].start").value("09:00"))
                .andExpect(jsonPath("$.reservation-time[1].start").value("11:00"))
                .andExpect(jsonPath("$.reservation-time[2].start").value("15:00"));
    }

    @Test
    public void bookBoardRoomTest() throws Exception {
        String submissionTime = "2011-03-17 10:17:06";
        String meetingTime = "2011-03-21 09:00";
        String employeeId = "EMP001";
        Integer duration = 2;

        MeetingInformationTestDto meeting = createMeeting(submissionTime, employeeId, meetingTime, duration);

        postRequest(meeting)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status[0].day").value("2011-03-21"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0]").exists())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("09:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].end").value("11:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].employeeId").value("EMP001"));
    }

    @Test
    public void outsideWorkingHoursTest() throws Exception {
        String meetingTime1 = "2011-03-21 16:00";
        String meetingTime2 = "2011-03-21 08:00";

        String submissionTime = "2011-03-17 10:17:06";
        String employeeId = "EMP001";
        Integer duration = 2;

        MeetingInformationTestDto meeting1 = createMeeting(submissionTime, employeeId, meetingTime1, duration);
        MeetingInformationTestDto meeting2 = createMeeting(submissionTime, employeeId, meetingTime2, duration);

        postRequest(meeting1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isEmpty());
        postRequest(meeting2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isEmpty());
    }

    @Test
    public void meetingTimeOverlapTest() throws Exception {
        String employeeId = "EMP001";
        Integer duration = 2;

        String meetingTime1 = "2011-03-21 13:00";
        String submissionTime1 = "2011-03-17 13:17:06";

        // FIRST CASE:
        // ------&&----**----&&----**------
        String meetingTime2 = "2011-03-21 14:00";
        String submissionTime2 = "2011-03-17 15:33:18";

        // SECOND CASE:
        // ------**----&&----**----&&------
        String meetingTime3 = "2011-03-21 12:00";
        String submissionTime3 = "2011-03-17 16:33:18";


        MeetingInformationTestDto meeting1 = createMeeting(submissionTime1, employeeId, meetingTime1, duration);
        MeetingInformationTestDto meeting2 = createMeeting(submissionTime2, employeeId, meetingTime2, duration);
        MeetingInformationTestDto meeting3 = createMeeting(submissionTime3, employeeId, meetingTime3, duration);

        postRequest(meeting1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time.length()", is(1)));

        postRequest(meeting2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time.length()", is(1)));

        postRequest(meeting3)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time.length()", is(1)));

    }

    @Test
    public void overwritingTimeTest() throws Exception {
        Integer duration = 2;

        String meetingTime1 = "2011-03-21 11:00";
        String submissionTime1 = "2011-03-17 13:17:06";
        String employeeId1 = "EMP001";

        String meetingTime2 = "2011-03-21 10:00";
        String submissionTime2 = "2011-03-10 15:33:18";
        String employeeId2 = "EMP002";

        MeetingInformationTestDto meeting1 = createMeeting(submissionTime1, employeeId1, meetingTime1, duration);
        MeetingInformationTestDto meeting2 = createMeeting(submissionTime2, employeeId2, meetingTime2, duration);

        postRequest(meeting1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("11:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].end").value("13:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].employeeId").value("EMP001"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time.length()", is(1)));

        postRequest(meeting2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].start").value("10:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].end").value("12:00"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time[0].employeeId").value("EMP002"))
                .andExpect(jsonPath("$.booking-status[0].reservation-time.length()", is(1)));
    }

    @Test
    public void chronologicalOrderOfBookingTest() throws Exception {
        String employeeId = "EMP001";
        Integer duration = 2;

        String meetingTime1 = "2011-03-21 13:00";
        String submissionTime1 = "2011-03-17 09:17:06";

        String meetingTime2 = "2011-03-17 14:00";
        String submissionTime2 = "2011-03-17 10:33:18";

        String meetingTime3 = "2011-03-19 10:00";
        String submissionTime3 = "2011-03-17 11:33:18";

        String meetingTime4 = "2011-03-19 15:00";
        String submissionTime4 = "2011-03-17 12:33:18";

        String meetingTime5 = "2011-03-19 13:00";
        String submissionTime5 = "2011-03-17 12:33:18";


        MeetingInformationTestDto meeting1 = createMeeting(submissionTime1, employeeId, meetingTime1, duration);
        MeetingInformationTestDto meeting2 = createMeeting(submissionTime2, employeeId, meetingTime2, duration);
        MeetingInformationTestDto meeting3 = createMeeting(submissionTime3, employeeId, meetingTime3, duration);
        MeetingInformationTestDto meeting4 = createMeeting(submissionTime4, employeeId, meetingTime4, duration);
        MeetingInformationTestDto meeting5 = createMeeting(submissionTime5, employeeId, meetingTime5, duration);

        postRequest(meeting1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status.length()", is(1)))
                .andExpect(jsonPath("$.booking-status[0].day").value("2011-03-21"));

        postRequest(meeting2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status.length()", is(2)))
                .andExpect(jsonPath("$.booking-status[0].day").value("2011-03-17"))
                .andExpect(jsonPath("$.booking-status[1].day").value("2011-03-21"));

        postRequest(meeting3)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status.length()", is(3)))
                .andExpect(jsonPath("$.booking-status[0].day").value("2011-03-17"))
                .andExpect(jsonPath("$.booking-status[1].day").value("2011-03-19"))
                .andExpect(jsonPath("$.booking-status[2].day").value("2011-03-21"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time.length()", is(1)))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[0].start").value("10:00"));

        postRequest(meeting4)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status.length()", is(3)))
                .andExpect(jsonPath("$.booking-status[1].day").value("2011-03-19"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time.length()", is(2)))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[0].start").value("10:00"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[1].start").value("15:00"));


        postRequest(meeting5)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking-status").isNotEmpty())
                .andExpect(jsonPath("$.booking-status.length()", is(3)))
                .andExpect(jsonPath("$.booking-status[1].day").value("2011-03-19"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time.length()", is(3)))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[0].start").value("10:00"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[1].start").value("13:00"))
                .andExpect(jsonPath("$.booking-status[1].reservation-time[2].start").value("15:00"));

    }

    private ResultActions postRequest(MeetingInformationTestDto meeting) throws Exception {
        return mockMvc.perform(post("/booking").content(json(meeting)));
    }

    private MeetingInformationTestDto createMeeting(String submissionTime, String employeeId,
                                                    String meetingTime, Integer duration) {

        WorkingHoursTestDto workingHours = new WorkingHoursTestDto(WORKING_START_TIME, WORKING_END_TIME);
        BookingInformationTestDto bookingInformation = new BookingInformationTestDto(submissionTime, employeeId, meetingTime, duration);

        MeetingInformationTestDto meeting = new MeetingInformationTestDto();
        meeting.setWorkingHours(workingHours);
        meeting.setBookingInformation(bookingInformation);
        return meeting;
    }

    /**
     * Serialize an arbitrary object into a JSON string.
     *
     * @param object source object
     * @return JSON string representing the object
     * @throws JsonProcessingException when something went wrong
     */
    private String json(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}