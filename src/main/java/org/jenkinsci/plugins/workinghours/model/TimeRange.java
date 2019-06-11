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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private TimeRangeDataContainer dataContainer = null;
    private String jsonData = "";
    private static Gson gson = null;

    /**
     * Get the actual time range data.
     * @return TimeRangeDataContainer
     */
    public TimeRangeDataContainer getDataContainer() {
        return dataContainer;
    }

    /**
     * Get the json data and could be passed to front end.
     * @return The source json data.
     */
    public String getJsonData() {
        return jsonData;
    }

    /**
     * Constructs a TimeRange object.
     *
     * @param jsonData The json that explains the time range.
     */
    @DataBoundConstructor
    public TimeRange(String jsonData) {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        this.jsonData = jsonData;
        this.dataContainer = gson.fromJson(jsonData,TimeRangeDataContainer.class);
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
     * Check whether configured rule includes a date.
     *
     * @param date date to check
     * @return true if date is inside of configured rule.
     */
    public Boolean includesTime(Calendar date) {
        // TODO: 12/6/2019 Implement include judge
//        LocalTime allowedStartTime = DateTimeUtility.localTime(getStartTime());
//        LocalTime allowedEndTime = DateTimeUtility.localTime(getEndTime());
//
//        LocalTime checkTime = LocalTime.of(
//                date.get(Calendar.HOUR_OF_DAY),
//                date.get(Calendar.MINUTE));
//
//        return date.get(Calendar.DAY_OF_WEEK) == getDayOfWeek()
//                && (checkTime.equals(allowedStartTime)
//                || checkTime.isAfter(allowedStartTime))
//                && (checkTime.equals(allowedEndTime)
//                || checkTime.isBefore(allowedEndTime));
        return true;
    }


    public static class TimeRangeDataContainer {

        /*The start time of the time range, like 00:00*/
        public String startTime = "";

        /*The end time of the time range, like 23:59*/
        public String endTime = "";

        /*The day of week*/
        public int dayOfWeek = Calendar.SUNDAY;
    }
}
