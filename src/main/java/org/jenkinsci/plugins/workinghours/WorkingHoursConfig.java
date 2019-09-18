/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 GoDaddy Operating Company, LLC.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.workinghours;

import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import hudson.Extension;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import jenkins.model.GlobalConfiguration;

/**
 * Provides configuration options for this plugin.
 *
 * @author jxpearce@godaddy.com
 */
@Extension(optional = true)
public class WorkingHoursConfig extends GlobalConfiguration {

    /**
     * The list of valid times.
     */
    private List<TimeRange> buildTimeMatrix;

    /**
     * The list of excluded dates.
     */
    private List<ExcludedDate> excludedDates;

    /**
     *  Custom string to display when job is blocked
     */
    private String customJobHoldText;

    /**
     * Default times for new configurations.
     */
    private final TimeRange[] defaultConfig = {
        new TimeRange("8:00", "18:00", Calendar.MONDAY),
        new TimeRange("8:00", "18:00", Calendar.TUESDAY),
        new TimeRange("8:00", "18:00", Calendar.WEDNESDAY),
        new TimeRange("8:00", "18:00", Calendar.THURSDAY),
        new TimeRange("8:00", "18:00", Calendar.FRIDAY)
    };

    /**
     * Convenience method to get the configuration object.
     *
     * @return the configuration object
     */
    public static WorkingHoursConfig get() {
        return GlobalConfiguration.all().get(WorkingHoursConfig.class);
    }

    /**
     * Default constructor - loads the configuration.
     */
    public WorkingHoursConfig() {
        load();
    }

    /**
     * Gets human readable name.
     *
     * @return human readable name
     */
    @Override
    public final String getDisplayName() {
        return Messages.WorkingHoursConfig_DisplayName();
    }

    /**
     * Gets the list of included times.
     *
     * @return list of included times.
     */
    public List<TimeRange> getBuildTimeMatrix() {
        return this.buildTimeMatrix == null
                ? Arrays.asList(defaultConfig)
                : this.buildTimeMatrix;
    }

    /**
     * Sets the list of included times.
     *
     * @param value the list of included times.
     */
    public void setBuildTimeMatrix(
            @CheckForNull List<TimeRange> value) {
        this.buildTimeMatrix = value;
        save();
    }

    /**
     * Gets the list of excluded dates.
     *
     * @return the list of excluded dates.
     */
    public List<ExcludedDate> getExcludedDates() {
        return this.excludedDates == null
                ? Collections.<ExcludedDate>emptyList()
                : this.excludedDates;
    }

    /**
     * Sets the list of excluded dates.
     *
     * @param value the list of excluded dates.
     */
    public void setExcludedDates(
            @CheckForNull List<ExcludedDate> value) {
        this.excludedDates = value;
        save();
    }

    /**
     * Gets the message to be displayed when the job is blocked
     *
     * @return custom message or the default message, if the custom message is null.
     */
    public String getJobHoldText() {
        return customJobHoldText == null
                ? Messages.WorkingHoursQueueTaskDispatcher_Offline()
                : customJobHoldText;
    }

    /**
     *  Sets new string to display when jobs are blocked
     * @param customJobHoldText string to use as the jobHeldDescription
     */
    public void setCustomJobHoldText(String customJobHoldText) {
        this.customJobHoldText = customJobHoldText.trim().length() > 0 ? customJobHoldText : null;
        save();
    }
}
