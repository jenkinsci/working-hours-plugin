/*
 * Copyright (c) 2019.
 */

package org.jenkinsci.plugins.workinghours.presets;

import org.jenkinsci.plugins.workinghours.model.Holiday;

import java.util.List;

public interface HolidayManager {
    List<Holiday> getHolidayThisYear();

    Holiday getCertainHolidayThisYear(String holidayKey);
}
