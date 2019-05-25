/*
 * The MIT License
 *
 * Copyright 2018 jxpearce.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package test.org.jenkinsci.plugins.workinghours.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.TimeRange;

/**
 * Helper methods to create different types for time ranges for use in testing.
 */
public class TimeRangeUtility {
    /**
     * Helper function to configure a set of time ranges that include the
     * current date/time
     *
     * @return List of time ranges
     */
    public static List<TimeRange> getInclusiveRange() {
        List<TimeRange> result = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        Calendar oneHourFromNow = (Calendar) now.clone();
        oneHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

        result.add(new TimeRange(formattedTime(now), formattedTime(oneHourFromNow), now.get(Calendar.DAY_OF_WEEK)));

        return result;
    }

    /**
     * Helper function to configure a set of time ranges that exclude the
     * current date/time
     *
     * @return List of time ranges
     */
    public static List<TimeRange> getExclusiveRange() {
        List<TimeRange> result = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        Calendar oneHourBeforeNow = (Calendar) now.clone();
        oneHourBeforeNow.add(Calendar.HOUR_OF_DAY, -1);
        Calendar twoHoursBeforeNow = (Calendar) now.clone();
        twoHoursBeforeNow.add(Calendar.HOUR_OF_DAY, -1);

        result.add(new TimeRange(formattedTime(twoHoursBeforeNow), formattedTime(oneHourBeforeNow), now.get(Calendar.DAY_OF_WEEK)));

        return result;
    }

    /**
     * Helper function to configure a set of excluded dates which doesn't contain today.
     *
     * @return List of excluded dates.
     */

    public static List<ExcludedDate> getInclusiveDate() {
        List<ExcludedDate> result = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

//        result.add(new ExcludedDate("test exclude date", formattedDate(tomorrow)));

        return result;
    }

    /**
     * Helper function to configure a set of excluded dates that exclude today.
     *
     * @return List of excluded dates.
     */
    public static List<ExcludedDate> getExclusiveDate() {
        List<ExcludedDate> result = new ArrayList<>();

        Calendar now = Calendar.getInstance();

//        result.add(new ExcludedDate("test exclude date", formattedDate(now)));

        return result;
    }

    /**
     * Helper function to configure a set of time ranges with one range that
     * includes the current time, but references a different day
     *
     * @return List of time ranges
     */
    public static List<TimeRange> getDifferentDayRange() {
        List<TimeRange> result = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -1);
        Calendar oneHourFromNow = (Calendar) now.clone();
        oneHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

        result.add(new TimeRange(formattedTime(now), formattedTime(oneHourFromNow), now.get(Calendar.DAY_OF_WEEK)));

        return result;
    }

    /**
     * Gets a formatted time string for a calendar instance.
     * @param cal the calender.
     * @return formatted time string.
     */
    public static String formattedTime(Calendar cal) {
        Date time = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("H:m");
        return format.format(time);
    }

    /**
     * Gets a formatted date string for a calendar instance.
     * @param cal the calender.
     * @return formatted date string.
     */
    public static String formattedDate(Calendar cal) {
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("M/d/y");
        return format.format(date);
    }

}
