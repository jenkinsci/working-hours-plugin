package org.jenkinsci.plugins.workinghours.model;

import org.jenkinsci.plugins.workinghours.presets.ChineseHolidayManager;
import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Holiday {
    protected String name;
    protected Date nextOccurrence;
    protected String key;

    /**
     * Construct holiday with a holiday from jollyday.
     *
     * @param jollyday {@link de.jollyday.Holiday} Source jollyday holiday.
     */
    public Holiday(de.jollyday.Holiday jollyday) {
        this.name = jollyday.getDescription();
        this.key = jollyday.getPropertiesKey();
        final LocalDate dateOfJollyday = LocalDate.of(
            jollyday.getDate().getYear(),
            jollyday.getDate().getMonthOfYear(),
            jollyday.getDate().getDayOfMonth()
        );
        this.nextOccurrence = new GregorianCalendar(dateOfJollyday.getYear(), dateOfJollyday.getMonth().getValue(), dateOfJollyday.getDayOfMonth()).getTime();
    }

    /**
     * Construct holiday from a chinese lunar holiday.
     *
     * @param chineseLunarHoliday {@link ChineseLunarCalendar} Source holiday.
     */
    public Holiday(ChineseHolidayManager.ChineseLunarHoliday chineseLunarHoliday) {
        this.name = chineseLunarHoliday.getName();

    }

    protected Holiday() {
    }

    /**
     * Get a {@link Holiday} from two {@link de.jollyday.Holiday}, compare their time with today and return the next occurrence.
     *
     * @param jollydayThisYear This year's holiday.
     * @param jollydayNextYear Next year's holiday.
     * @return Next Occurrence.
     */
    private static Holiday getHolidayFromTwoJollyDay(de.jollyday.Holiday jollydayThisYear, de.jollyday.Holiday jollydayNextYear) {
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

    /**
     * Merge two list of {@link de.jollyday.Holiday} into a list of {@link Holiday}
     *
     * @param resultThisYear This year's holidays.
     * @param resultNextYear Next year's holidays.
     * @return List of holidays whose next occurrence is resolved.
     */
    public static List<Holiday> mergeTwoYearsHoliday(List<de.jollyday.Holiday> resultThisYear, List<de.jollyday.Holiday> resultNextYear) {
        List<Holiday> holidays = new ArrayList<>();
        for (int i = 0; i < resultThisYear.size(); i++) {
            final Holiday holiday = getHolidayFromTwoJollyDay(resultThisYear.get(i), resultNextYear.get(i));
            holidays.add(holiday);
        }
        return holidays;
    }

    /**
     * Get holiday's name.
     *
     * @return Holiday's name.
     */
    public String getName() {
        return name;
    }


    /**
     * Get a holidays key to identify it.
     *
     * @return Holiday's key.
     */
    public String getKey() {
        return key;
    }


    /**
     * Get holiday's next occurrence.
     *
     * @return Holiday's next occurrence.
     */
    public long getNextOccurrence() {
        return nextOccurrence.getTime();
    }
}
