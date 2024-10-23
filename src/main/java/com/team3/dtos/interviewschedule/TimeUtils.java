package com.team3.dtos.interviewschedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static String formatTimeTo12Hour(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return time.format(formatter);
    }
}