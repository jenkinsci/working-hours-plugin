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
package org.jenkinsci.plugins.workinghours.model;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * Encapsulates an excluded date along with name for UI purposes.
 *
 * @author jxpearce@godaddy.com
 */
public class ExcludedDate extends AbstractDescribableImpl<ExcludedDate> {

    /**
     * The display name of this rule.
     */
    private String name;

    /**
     * The date for this rule.
     */
    private String date;

    /**
     * Whether or not this date applies every year
     */
    private boolean repeatYearly;

    /**
     * @deprecated use {@link #ExcludedDate(String, String, boolean)} instead}
     *
     * Constructs an ExcludedDate object.
     *
     * @param name Display name for date, e.g. "New Year's Day".
     * @param date The date to exclude.
     */
    @Deprecated
    public ExcludedDate(final String name, final String date) {
        this(name, date, false);
    }

    /**
     * Constructs an ExcludedDate object.
     *
     * @param name Display name for date, e.g. "New Year's Day".
     * @param date The date to exclude.
     * @param repeatYearly Whether or not to check the year when excluding
     */
    @DataBoundConstructor
    public ExcludedDate(final String name, final String date, final boolean repeatYearly) {
        this.name = name;
        this.date = date;
        this.repeatYearly = repeatYearly;
    }

    /**
     * Get the value of name.
     *
     * @return the value of name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Get the value of date.
     *
     * @return the value of date.
     */
    public final String getDate() {
        return date;
    }

    /**
     * Get the value of repeatYearly.
     *
     * @return the value of repeatYearly.
     */
    public final boolean isRepeatYearly() {
        return repeatYearly;
    }

    /**
     * Set the value of date.
     *
     * @param date new value of date.
     */
    public final void setDate(final String date) {
        this.date = date;
    }

    /**
     * Set the value of repeatYearly.
     *
     * @param repeatYearly new value of repeatYearly.
     */
    public final void setRepeatYearly(final boolean repeatYearly) { this.repeatYearly = repeatYearly; }

    /**
     * Set the value of name.
     *
     * @param name new value of name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Descriptor class required for the UI.
     */
    @Extension
    public static class DescriptorImpl extends Descriptor<ExcludedDate> {

        /**
         * Gets human readable name.
         *
         * @return human readable name.
         */
        @Override
        public final String getDisplayName() {
            return "ExcludedDate descriptor";
        }

        /**
         * Validate the date.
         *
         * @param value value to validate.
         * @return FormValidation indicating whether date is valid.
         */
        public final FormValidation doCheckDate(
                @QueryParameter final String value) {
            if (!value.isEmpty() && !DateTimeUtility.isValidDate(value)) {
                return FormValidation.error("Invalid date");
            }
            return FormValidation.ok();
        }
    }

    public Boolean shouldExclude(Calendar date) {
        LocalDate localDate = DateTimeUtility.localDate(getDate());

        if (localDate.getDayOfMonth() == date.get(Calendar.DAY_OF_MONTH)
                && localDate.getMonthValue() == date.get(Calendar.MONTH) + 1
                && (localDate.getYear() == date.get(Calendar.YEAR) || isRepeatYearly())) {
            return true;
        }
        return false;
    }
}
