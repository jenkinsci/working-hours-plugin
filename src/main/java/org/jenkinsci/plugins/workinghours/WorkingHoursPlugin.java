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

import hudson.ExtensionList;
import hudson.Plugin;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.GlobalConfiguration;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import hudson.Extension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Provides configuration options for this plugin.
 *
 * @author jxpearce@godaddy.com
 */
@Extension(optional = true)
public class WorkingHoursPlugin extends Descriptor<WorkingHoursPlugin> implements Describable<WorkingHoursPlugin> {

    /**
     * The list of valid times.
     */
    private List<TimeRange> buildTimeMatrix;

    /**
     * The list of excluded dates.
     */
    private List<ExcludedDate> excludedDates;

    /**
     * Default times for new configurations.
     */
    private final TimeRange[] defaultConfig = {
    };

    /**
     * Convenience method to get the configuration object.
     *
     * @return the configuration object
     */

    /**
     * Default constructor - loads the configuration.
     */
    public WorkingHoursPlugin() {
        super(self());
        load();
    }

    /**
     * Gets the list of included times.
     *
     * @return list of included times.
     */
    public List<TimeRange> getBuildTimeMatrix() {
        return this.buildTimeMatrix == null?
            Collections.<TimeRange>emptyList()
            : this.buildTimeMatrix;
    }

    /**
     * Hide it from the system configure page by returning "".
     * @return "" empty string to hide it from the system configure page.
     */
    @Nonnull
    @Override
    public String getDisplayName() {
        return "";
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

    @Override
    public Descriptor<WorkingHoursPlugin> getDescriptor() {
        return this;
    }
}
