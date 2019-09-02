package org.jenkinsci.plugins.workinghours.utils;

import de.jollyday.Holiday;
import de.jollyday.HolidayManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Helper methods when using jollyday.
 */
public class JollydayUtil {

    /**
     * Get two years holiday, so we could show the next occurrence if this year's holiday is past.
     *
     * @param regionCode Code of the target region whose holidays should be returned.
     * @return Set with two years holidays.
     */

    public static List<org.jenkinsci.plugins.workinghours.model.Holiday> getTwoYearsHoliday(String regionCode) {
        List resultThisYear = new ArrayList<>(HolidayManager.getInstance(regionCode).getHolidays(Calendar.getInstance().get(Calendar.YEAR)));
        resultThisYear.sort((Comparator<Holiday>) (day1, day2) -> {
            if (day1.getDate().isBefore(day2.getDate())) {
                return -1;
            } else if (day1.getDate().isEqual(day2.getDate())) {
                return 0;
            } else {
                return 1;
            }
        });

        List resultNextYear = new ArrayList<>(HolidayManager.getInstance(regionCode).getHolidays(Calendar.getInstance().get(Calendar.YEAR) + 1));
        resultNextYear.sort((Comparator<Holiday>) (day1, day2) -> {
            if (day1.getDate().isBefore(day2.getDate())) {
                return -1;
            } else if (day1.getDate().isEqual(day2.getDate())) {
                return 0;
            } else {
                return 1;
            }
        });

        return org.jenkinsci.plugins.workinghours.model.Holiday.mergeTwoYearsHoliday(resultThisYear, resultNextYear);
    }

    /**
     * Get a certain holiday this year.
     *
     * @param regionCode The region's code of the holiday.
     * @param holidayKey Key of the target holiday.
     * @return {@link Holiday} The target holiday.
     */
    public static Holiday getHolidayThisYear(String regionCode, String holidayKey) {
        Thread t = Thread.currentThread();
        ClassLoader orig = t.getContextClassLoader();
        t.setContextClassLoader(HolidayManager.class.getClassLoader());
        try {
            return HolidayManager.getInstance(regionCode).getHolidays(Calendar.getInstance().get(Calendar.YEAR)).stream().filter(holiday -> holiday.getPropertiesKey().equals(holidayKey)).findFirst().get();
        } finally {
            t.setContextClassLoader(orig);
        }
    }

    /**
     * Get a certain holiday next year.
     *
     * @param regionCode The region's code of the holiday.
     * @param holidayKey Key of the target holiday.
     * @return {@link Holiday} The target holiday.
     */
    public static Holiday getHolidayNextYear(String regionCode, String holidayKey) {
        Thread t = Thread.currentThread();
        ClassLoader orig = t.getContextClassLoader();
        t.setContextClassLoader(HolidayManager.class.getClassLoader());
        try {
            return HolidayManager.getInstance(regionCode).getHolidays(Calendar.getInstance().get(Calendar.YEAR)+1).stream().filter(holiday -> holiday.getPropertiesKey().equals(holidayKey)).findFirst().get();
        } finally {
            t.setContextClassLoader(orig);
        }
    }

    /**
     * Get a certain holiday in a certain year.
     *
     * @param regionCode The region's code of the holiday.
     * @param holidayKey Key of the target holiday.
     * @param year The certain year to specify.
     * @return {@link Holiday} The target holiday.
     */
    public static Holiday getCertainYearsHoliday(String regionCode, String holidayKey,int year) {
        Thread t = Thread.currentThread();
        ClassLoader orig = t.getContextClassLoader();
        t.setContextClassLoader(HolidayManager.class.getClassLoader());
        try {
            return HolidayManager.getInstance(regionCode).getHolidays(year).stream().filter(holiday -> holiday.getPropertiesKey().equals(holidayKey)).findFirst().get();
        } finally {
            t.setContextClassLoader(orig);
        }
    }
}

