package org.jenkinsci.plugins.workinghours.presets;

import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChineseHolidayManager {

    private static ChineseHolidayManager instance;

    static final String REGION_CODE = "CN";

    private List<Holiday> chineseLunarHolidays;

    public List<Holiday> getHolidayThisYear() {
        for (Holiday chineseLunarHoliday : chineseLunarHolidays) {
            ((ChineseLunarHoliday) chineseLunarHoliday).updateNextOccurrence();
        }
        return this.chineseLunarHolidays;
    }

    private ChineseHolidayManager() {
        chineseLunarHolidays = new ArrayList<>();
        chineseLunarHolidays.add(new ChineseLunarHoliday("Spring Festival", 1, 1));
        chineseLunarHolidays.add(new ChineseLunarHoliday("Lantern Festival", 1, 15));
        chineseLunarHolidays.add(new ChineseLunarHoliday("Dragon Boat Festival", 5, 5));
        chineseLunarHolidays.add(new ChineseLunarHoliday("Mid-Autumn Festival", 8, 15));
    }

    public static ChineseHolidayManager getInstance() {
        if (instance == null) {
            instance = new ChineseHolidayManager();
        }
        return instance;
    }


    public static class ChineseLunarHoliday extends Holiday {
        private int monthOfYear;
        private int dayOfMonth;

        ChineseLunarHoliday(final String name, final int monthOfYear, final int dayOfMonth) {
            this.name = name;
            this.key = name.toUpperCase().replace(" ", "_");
            this.monthOfYear = monthOfYear;
            this.dayOfMonth = dayOfMonth;
            this.updateNextOccurrence();
        }

        public int getMonthOfYear() {
            return monthOfYear;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }

        /*Update next occurrence according to now.*/
        void updateNextOccurrence() {
            final Date occurrenceThisYear = ChineseLunarCalendar.lunar2Solar(Calendar.getInstance().get(Calendar.YEAR), this.getMonthOfYear(), this.getDayOfMonth(), false).getTime();
            if (occurrenceThisYear.after(new Date())) {
                this.nextOccurrence = occurrenceThisYear;
            } else {
                this.nextOccurrence = ChineseLunarCalendar.lunar2Solar(Calendar.getInstance().get(Calendar.YEAR) + 1, this.getMonthOfYear(), this.getDayOfMonth(), false).getTime();
            }
        }
    }
}
