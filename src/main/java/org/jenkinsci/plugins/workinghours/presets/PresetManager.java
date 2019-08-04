package org.jenkinsci.plugins.workinghours.presets;

import de.jollyday.HolidayCalendar;
import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.utils.JollydayUtil;

import java.util.ArrayList;
import java.util.List;

public class PresetManager {


    private static PresetManager instance;

    public static PresetManager getInstance() {
        if (instance == null) {
            instance = new PresetManager();
        }
        return instance;
    }

    private PresetManager() {

    }

    public List<String> getRegions() {
        List<String> calendars = new ArrayList<>();
        for (HolidayCalendar calendar : HolidayCalendar.values()) {
            calendars.add(calendar.getId());
        }
        calendars.add(ChineseHolidayManager.REGION_CODE);
        return calendars;
    }

    public List<Holiday> getRegionHolidays(String region) {
        if (region.equals(ChineseHolidayManager.REGION_CODE)) {
            return ChineseHolidayManager.getInstance().getHolidayThisYear();
        }
        return JollydayUtil.getTwoYearsHoliday(region);
    }
}
