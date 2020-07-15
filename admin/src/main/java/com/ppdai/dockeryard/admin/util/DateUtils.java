package com.ppdai.dockeryard.admin.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;

public final class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
        throw new IllegalAccessError();
    }

    public static LocalDateTime currentTimeMinus(long amountToSubtract, TemporalUnit unit) {
        return LocalDateTime.now().minus(amountToSubtract, unit);
    }

    public static String localDateTimeFormat(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

}
