package org.jenkinsci.plugins.workinghours.presets;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.utils.JollydayUtil;

import java.util.ArrayList;
import java.util.List;

public class PresetManager {

    /*Singleton*/
    private static PresetManager instance;

    public static PresetManager getInstance() {
        if (instance == null) {
            instance = new PresetManager();
        }
        return instance;
    }

    private PresetManager() {

    }

    /**
     * Get available regions.
     *
     * @return {@link List} A list of region codes.
     */
    public List<String> getRegions() {
        List<String> calendars = new ArrayList<>();
        for (HolidayCalendar calendar : HolidayCalendar.values()) {
            calendars.add(calendar.getId());
        }
        calendars.add(ChineseHolidayManager.REGION_CODE);
        return calendars;
    }

    /**
     * Get regional holidays according to the given region code.
     *
     * @param regionCode Code of the region.
     * @return {@link List} Region's holidays.
     */
    public List<? extends Holiday> getRegionHolidays(String regionCode) {
        Thread t = Thread.currentThread();
        ClassLoader orig = t.getContextClassLoader();
        t.setContextClassLoader(HolidayManager.class.getClassLoader());
        try {
            if (regionCode.equals(ChineseHolidayManager.REGION_CODE)) {
                return ChineseHolidayManager.getInstance().getHolidayThisYear();
            } else {
                return JollydayUtil.getTwoYearsHoliday(regionCode);
            }
        } finally {
            t.setContextClassLoader(orig);
        }
    }
}
