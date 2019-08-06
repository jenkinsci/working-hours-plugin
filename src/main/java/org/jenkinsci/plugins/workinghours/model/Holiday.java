package org.jenkinsci.plugins.workinghours.model;

import org.jenkinsci.plugins.workinghours.presets.ChineseHolidayManager;
import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;

import java.time.LocalDate;
import java.util.*;

public class Holiday {
    protected String name;
    protected Date nextOccurrence;

    public Holiday(de.jollyday.Holiday jollyday) {
        this.name = jollyday.getDescription();
        final LocalDate dateOfJollyday = LocalDate.of(
            jollyday.getDate().getYear(),
            jollyday.getDate().getMonthOfYear(),
            jollyday.getDate().getDayOfMonth()
        );
        this.nextOccurrence = new GregorianCalendar(dateOfJollyday.getYear(), dateOfJollyday.getMonth().getValue(), dateOfJollyday.getDayOfMonth()).getTime();

    }

    public Holiday(ChineseHolidayManager.ChineseLunarHoliday chineseLunarHoliday) {
        this.name = chineseLunarHoliday.getName();
        final Date occurrenceThisYear = ChineseLunarCalendar.lunar2Solar(Calendar.getInstance().get(Calendar.YEAR), chineseLunarHoliday.getMonthOfYear(), chineseLunarHoliday.getDayOfMonth(), false).getTime();
        if (occurrenceThisYear.after(new Date())) {
            this.nextOccurrence = occurrenceThisYear;
        } else {
            this.nextOccurrence = ChineseLunarCalendar.lunar2Solar(Calendar.getInstance().get(Calendar.YEAR) + 1, chineseLunarHoliday.getMonthOfYear(), chineseLunarHoliday.getDayOfMonth(), false).getTime();
        }
    }

    public Holiday() {
    }

    public static Holiday getHolidayFromTwoJollyDay(de.jollyday.Holiday jollydayThisYear, de.jollyday.Holiday jollydayNextYear) {
        final LocalDate now = LocalDate.now();
        final LocalDate jollydayThisYearDate = LocalDate.of(
            jollydayThisYear.getDate().getYear(),
            jollydayThisYear.getDate().getMonthOfYear(),
            jollydayThisYear.getDate().getDayOfMonth()
        );
        if (jollydayThisYearDate.isAfter(now) || jollydayThisYearDate.isEqual(now)) {
            return new Holiday(jollydayThisYear);
        } else {
            return new Holiday(jollydayNextYear);
        }
    }

    public static Holiday getHolidayFromChineseHoliday() {
        return null;
    }

    public static List<Holiday> mergeTwoYearsHoliday(List<de.jollyday.Holiday> resultThisYear, List<de.jollyday.Holiday> resultNextYear) {
        List<Holiday> holidays = new ArrayList<>();
        for (int i = 0; i < resultThisYear.size(); i++) {
            final Holiday holiday = getHolidayFromTwoJollyDay(resultThisYear.get(i), resultNextYear.get(i));
            holidays.add(holiday);
        }
        return holidays;
    }

    public static List<Holiday> fromChineseLunarHoliday(List<ChineseHolidayManager.ChineseLunarHoliday> chineseLunarHolidays) {

        return null;
    }

    public String getName() {
        return name;
    }

    public long getNextOccurrence() {
        return nextOccurrence.getTime();
    }
}
