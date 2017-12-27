package com.smartbics.booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        String time = "0900";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        LocalTime localDateTime = LocalTime.parse(time, timeFormatter);
        System.out.println(localDateTime);
    }
}
