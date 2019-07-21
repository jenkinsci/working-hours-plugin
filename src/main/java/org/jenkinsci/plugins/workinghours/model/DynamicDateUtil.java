package org.jenkinsci.plugins.workinghours.model;


import java.time.LocalDate;

public class DynamicDateUtil {
    public static LocalDate nextOccurrenceByMonth(int weekOfMonth, int dayOfWeek) {
        LocalDate next = LocalDate.now();
        LocalDate today = LocalDate.now();
        LocalDate nextOccurrenceInThisMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        int tempWeekOfMonth = weekOfMonth;
        if (nextOccurrenceInThisMonth.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = weekOfMonth - 1;
        }
        nextOccurrenceInThisMonth.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - nextOccurrenceInThisMonth.getDayOfMonth()));
        //If in same month but later
        if (nextOccurrenceInThisMonth.isEqual(today) ||
            nextOccurrenceInThisMonth.isAfter(today)
        ) {
            return nextOccurrenceInThisMonth;
        }
        //Add to next month
        next = next.plusMonths(1);
        //Also set the date to the start of the month.
        next.withDayOfMonth(1);
        if (next.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = tempWeekOfMonth - 1;
        }
        next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
        return next;
    }

    public static LocalDate nextOccurrenceByYear(int monthOfYear, int weekOfMonth, int dayOfWeek) {
        LocalDate next = LocalDate.now();
        LocalDate today = LocalDate.now();
        LocalDate nextOccurrenceInThisMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        int tempWeekOfMonth = weekOfMonth;
        if (nextOccurrenceInThisMonth.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = weekOfMonth - 1;
        }
        nextOccurrenceInThisMonth.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - nextOccurrenceInThisMonth.getDayOfMonth()));
        //If in same month but later
        if (nextOccurrenceInThisMonth.isEqual(today) ||
            nextOccurrenceInThisMonth.isAfter(today)
        ) {
            return nextOccurrenceInThisMonth;
        }
        //Add to next month
        next = next.plusMonths(1);
        //Also set the date to the start of the month.
        next.withDayOfMonth(1);
        if (next.getDayOfWeek().getValue() <= dayOfWeek) {
            tempWeekOfMonth = tempWeekOfMonth - 1;
        }
        next.withDayOfMonth(1 + (tempWeekOfMonth * 7) + (dayOfWeek - next.getDayOfWeek().getValue()));
        return next;
    }
}
