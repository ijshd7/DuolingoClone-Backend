package com.testingpractice.duoclonebackend.utils;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {

    public static LocalDate getDate () {
        ZoneId tz = ZoneId.systemDefault();
        return LocalDate.now(tz);
    }

}
