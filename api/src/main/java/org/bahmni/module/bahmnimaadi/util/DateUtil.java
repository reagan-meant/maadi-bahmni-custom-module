package org.bahmni.module.bahmnimaadi.util;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateUtil {
    public static Date startOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(YEAR),
                calendar.get(MONTH),
                calendar.get(DATE), 0, 0, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTime();
    }
}
