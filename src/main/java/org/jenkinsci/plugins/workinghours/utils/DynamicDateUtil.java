package org.jenkinsci.plugins.workinghours.utils;


import java.time.LocalDate;

public class DynamicDateUtil {
    /**
     * Get next occurrence, by month.
     *
     * @param weekOfMonth Week of month.
     * @param dayOfWeek   Day of week.
     * @return {@link LocalDate} Next occurrence.
     */
    public static LocalDate nextOccurrenceByMonth(final int weekOfMonth, final int dayOfWeek,final LocalDate now) {
        LocalDate next;
        LocalDate today;
        if (now != null) {
            next = now;
            today = now;
        } else {
            next = LocalDate.now();
            today = LocalDate.now();
        }
        LocalDate nextOccurrenceInThisMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        int tempWeekOfMonth = weekOfMonth;
        if (nextOccurrenceInThisMonth.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = weekOfMonth - 1;
        }
        nextOccurrenceInThisMonth = nextOccurrenceInThisMonth.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - nextOccurrenceInThisMonth.getDayOfMonth()));
        //If in same month but later
        if (nextOccurrenceInThisMonth.isEqual(today) ||
            nextOccurrenceInThisMonth.isAfter(today)
        ) {
            return nextOccurrenceInThisMonth;
        }

        //Add to next month
        next = next.plusMonths(1);
        //Also set the date to the start of the month.
        next = next.withDayOfMonth(1);
        if (next.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = weekOfMonth - 1;
        } else {
            tempWeekOfMonth = weekOfMonth;
        }
        next = next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
        return next;
    }

    /**
     * Get next occurrence, based on year.
     *
     * @param monthOfYear Month of year.
     * @param weekOfMonth Week of month.
     * @param dayOfWeek   Day of week.
     * @return {@link LocalDate} Next occurrence.
     */
    public static LocalDate nextOccurrenceByYear(final int monthOfYear, final int weekOfMonth, final int dayOfWeek, final LocalDate now) {
        LocalDate next;
        LocalDate today;
        if (now != null) {
            next = now;
            today = now;
        } else {
            next = LocalDate.now();
            today = LocalDate.now();
        }
        if (today.getMonth().getValue() > monthOfYear) {
            /*If the month has been passed.*/

            /*Reset to the begin of next year's target month.*/
            next = next.plusYears(1).withMonth(monthOfYear).withDayOfMonth(1);

            int tempWeekOfMonth = weekOfMonth;
            if (next.getDayOfWeek().getValue() <= dayOfWeek) {
                tempWeekOfMonth = weekOfMonth - 1;
            }

            next = next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
            //If in same month but later
            return next;
        } else if (today.getMonth().getValue() == monthOfYear) {
            /*If in this month*/
            LocalDate nextOccurrenceInThisMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
            int tempWeekOfMonth = weekOfMonth;
            if (nextOccurrenceInThisMonth.getDayOfWeek().getValue() <= dayOfWeek) {
                tempWeekOfMonth = weekOfMonth - 1;
            }

            nextOccurrenceInThisMonth = nextOccurrenceInThisMonth.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - nextOccurrenceInThisMonth.getDayOfMonth()));
            //If in same month but later
            if (nextOccurrenceInThisMonth.isEqual(today) ||
                nextOccurrenceInThisMonth.isAfter(today)
            ) {
                return nextOccurrenceInThisMonth;
            } else {
                /*Reset to the begin of next year's target month.*/
                next = next.plusYears(1).withMonth(monthOfYear).withDayOfMonth(1);

                tempWeekOfMonth = weekOfMonth;
                if (next.getDayOfWeek().getValue() <= dayOfWeek) {
                    tempWeekOfMonth = weekOfMonth - 1;
                }

                next = next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
                //If in same month but later
                return next;
            }
        } else {
            next = next.withMonth(monthOfYear).withDayOfMonth(1);

            int tempWeekOfMonth = weekOfMonth;
            if (next.getDayOfWeek().getValue() <= dayOfWeek) {
                tempWeekOfMonth = weekOfMonth - 1;
            }

            next = next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
            //If in same month but later
            return next;
        }
    }
}
