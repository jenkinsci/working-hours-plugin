package org.jenkinsci.plugins.workinghours.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Holiday {
    private String name;
    private Date nextOccurrence;

    private Holiday(de.jollyday.Holiday jollyday) {
        this.name = jollyday.getDescription();
        final LocalDate dateOfJollyday = LocalDate.of(
            jollyday.getDate().getYear(),
            jollyday.getDate().getMonthOfYear(),
            jollyday.getDate().getDayOfMonth()
        );
        this.nextOccurrence = new GregorianCalendar(dateOfJollyday.getYear(), dateOfJollyday.getMonth().getValue(), dateOfJollyday.getDayOfMonth()).getTime();

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

    public String getName() {
        return name;
    }

    public Date getNextOccurrence() {
        return nextOccurrence;
    }
}
