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
import hudson.util.ListBoxModel;
import java.text.DateFormatSymbols;
import java.time.LocalTime;
import java.util.Calendar;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Encapsulates a time range, which matches a times on a particular day of the
 * week that occur between a start and end time.
 *
 * @author jxpearce@godaddy.com
 */
public class TimeRange extends AbstractDescribableImpl<TimeRange> {

    /**
     * The range start time.
     */
    private String startTime = "";

    /**
     * The range end time.
     */
    private String endTime = "";

    /**
     * The day of the week for this range.
     */
    private int dayOfWeek;

    /**
     * Default constructor.
     */
    public TimeRange() {
        startTime = "";
        endTime = "";
        dayOfWeek = 0;
    }

    /**
     * Constructs a TimeRange object.
     *
     * @param startTime the range start time.
     * @param endTime the range end time.
     * @param dayOfWeek day of week to apply range.
     */
    @DataBoundConstructor
    public TimeRange(String startTime,
            String endTime,
            int dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Required descriptor class.
     */
    @Extension
    public static class DescriptorImpl extends Descriptor<TimeRange> {

        /**
         * Gets human readable name.
         *
         * @return human readable name.
         */
        @Override
        public String getDisplayName() {
            return "TimeRange descriptor";
        }

        /**
         * Validate the start time.
         *
         * @param value value to validation.
         * @return FormValidation indicating whether value is a valid time
         * string.
         */
        public FormValidation doCheckStartTime(
                @QueryParameter String value) {
            if (!value.equals("")) {
                if (!DateTimeUtility.isValidTime(value)) {
                    return FormValidation.error("Invalid start time");
                }
            }
            return FormValidation.ok();
        }

        /**
         * Validate the end time.
         *
         * @param value value to validation.
         * @return FormValidation indicating whether value is a valid time
         * string.
         */
        public FormValidation doCheckEndTime(
                @QueryParameter String value) {
            if (!value.equals("")) {
                if (!DateTimeUtility.isValidTime(value)) {
                    return FormValidation.error("Invalid end time");
                }
            }
            return FormValidation.ok();
        }

        /**
         * Called from form UI to fill the day select control.
         *
         * @return ListBoxModel describing valid options.
         */
        public ListBoxModel doFillDayOfWeekItems() {
            ListBoxModel model = new ListBoxModel();
            String[] dayNames = new DateFormatSymbols().getWeekdays();
            model.add(
                    dayNames[Calendar.MONDAY],
                    String.valueOf(Calendar.MONDAY));
            model.add(
                    dayNames[Calendar.TUESDAY],
                    String.valueOf(Calendar.TUESDAY));
            model.add(
                    dayNames[Calendar.WEDNESDAY],
                    String.valueOf(Calendar.WEDNESDAY));
            model.add(
                    dayNames[Calendar.THURSDAY],
                    String.valueOf(Calendar.THURSDAY));
            model.add(
                    dayNames[Calendar.FRIDAY],
                    String.valueOf(Calendar.FRIDAY));
            model.add(
                    dayNames[Calendar.SATURDAY],
                    String.valueOf(Calendar.SATURDAY));
            model.add(
                    dayNames[Calendar.SUNDAY],
                    String.valueOf(Calendar.SUNDAY));

            return model;
        }
    }

    /**
     * Get the value of startTime.
     *
     * @return the value of startTime.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Set the value of startTime.
     *
     * @param startTime new value of startTime.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Get the value of endTime.
     *
     * @return the value of endTime.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Set the value of endTime.
     *
     * @param endTime new value of endTime.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Get the value of dayOfWeek.
     *
     * @return the value of dayOfWeek.
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Set the value of dayOfWeek.
     *
     * @param dayOfWeek new value of dayOfWeek.
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Check whether configured rule includes a date.
     *
     * @param date date to check
     * @return true if date is inside of configured rule.
     */
    public Boolean includesTime(Calendar date) {
        LocalTime allowedStartTime = DateTimeUtility.localTime(getStartTime());
        LocalTime allowedEndTime = DateTimeUtility.localTime(getEndTime());

        LocalTime checkTime = LocalTime.of(
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE));

        return date.get(Calendar.DAY_OF_WEEK) == getDayOfWeek()
                && (checkTime.equals(allowedStartTime)
                || checkTime.isAfter(allowedStartTime))
                && (checkTime.equals(allowedEndTime)
                || checkTime.isBefore(allowedEndTime));

    }
}
