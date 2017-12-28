package com.smartbics.booking.controllers;

import com.smartbics.booking.beans.BookingCalendar;
import com.smartbics.booking.config.WebTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@EnableAutoConfiguration
@SpringBootTest(classes = {
        WebTestConfig.class
})
@ComponentScan(value = "com.smartbics.booking")
@RunWith(SpringRunner.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingCalendar bookingCalendar;

    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/booking"))
                .andExpect(status().isOk());

    }
}