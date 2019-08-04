package org.jenkinsci.plugins.workinghours.presets;

import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChineseHolidayManager {

    private static ChineseHolidayManager instance;

    static final String REGION_CODE = "CN";

    public List<Holiday> getHolidayThisYear() {
        ChineseLunarCalendar lunar = new ChineseLunarCalendar();
        Calendar today = ChineseLunarCalendar.lunar2Solar(lunar.getLyear(), lunar.getLmonth(), lunar.getLdate(), lunar.isLeapMonth());
        return new ArrayList<>();
    }

    private ChineseHolidayManager() {

    }

    public static ChineseHolidayManager getInstance() {
        if (instance == null) {
            instance = new ChineseHolidayManager();
        }
        return instance;
    }


    private static class ChineseHoliday {
        String name;
        int monthOfYear;
        int dayOfMonth;

        public ChineseHoliday(String name, int monthOfYear, int dayOfMonth) {
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
