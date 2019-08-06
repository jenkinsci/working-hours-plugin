package org.jenkinsci.plugins.workinghours.presets;

import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChineseHolidayManager{

    private static ChineseHolidayManager instance;

    static final String REGION_CODE = "CN";

    private static List<ChineseLunarHoliday> chineseLunarHolidays;

    public List<Holiday> getHolidayThisYear() {
        ChineseLunarCalendar lunar = new ChineseLunarCalendar();
        Calendar today = ChineseLunarCalendar.lunar2Solar(lunar.getLyear(), lunar.getLmonth(), lunar.getLdate(), lunar.isLeapMonth());
        final List<Holiday> holidays = new ArrayList<>();
        for (ChineseHolidayManager.ChineseLunarHoliday chineseLunarHoliday : chineseLunarHolidays) {
            holidays.add(new Holiday(chineseLunarHoliday));
        }
        return holidays;
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
        private String name;
        private int monthOfYear;
        private int dayOfMonth;

        public ChineseLunarHoliday(String name, int monthOfYear, int dayOfMonth) {
            super();
            this.name = name;
            this.monthOfYear = monthOfYear;
            this.dayOfMonth = dayOfMonth;
        }

        public String getName() {
            return name;
        }

        public int getMonthOfYear() {
            return monthOfYear;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }
    }
}
