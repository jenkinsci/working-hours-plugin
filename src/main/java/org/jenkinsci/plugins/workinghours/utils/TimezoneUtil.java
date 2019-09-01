/*
 * Copyright (c) 2019.
 */

package org.jenkinsci.plugins.workinghours.utils;

import hudson.ExtensionList;
import org.jenkinsci.plugins.workinghours.WorkingHoursPlugin;

public class TimezoneUtil {
    public static String getTimezone() {
        return ExtensionList.lookup(WorkingHoursPlugin.class).get(0).getTimezone();
    }

    public static int getUTCOffset() {
        return ExtensionList.lookup(WorkingHoursPlugin.class).get(0).getUtcOffset();
    }
}
