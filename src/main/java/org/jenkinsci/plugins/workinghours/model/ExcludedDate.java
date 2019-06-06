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
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Encapsulates an excluded date along with name for UI purposes.
 *
 * @author jxpearce@godaddy.com
 */
public class ExcludedDate extends AbstractDescribableImpl<ExcludedDate> {

    private DataContainer dataContainer = null;
    private String jsonData = "";
    private static Gson gson = null;

    /**
     * Set or update the fields with a json which is then
     */
    public void setJsonData(String inputData) {
        this.jsonData = inputData;
        this.dataContainer = gson.fromJson(inputData, DataContainer.class);
    }

    public String getJsonData() {
        return jsonData;
    }


    /**
     * Constructs an ExcludedDate object using json.
     *
     * @param jsonData Json data to deserialize to an ExcludedDate object.
     */
    @DataBoundConstructor
    public ExcludedDate(String jsonData) {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        this.dataContainer = gson.fromJson(jsonData, DataContainer.class);
        this.jsonData = jsonData;
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
//        LocalDate localDate = DateTimeUtility.localDate(getDate());

//        LocalDate checkTime = LocalDate.of(
//                date.get(Calendar.YEAR),
//                date.get(Calendar.MONTH) + 1,
//                date.get(Calendar.DAY_OF_MONTH));
//
//        return localDate.equals(checkTime);
        return false;
    }

    public static class DataContainer {

        /**
         * Minutes offset to the UTC time, indicates the base timezone of the excluded date.
         * Default to UTC(UTC+0).
         */
        public int utcOffset = 0;

        /**
         * A type that indicates whether
         */
        public String type = DateType.TYPE_GREGORIAN;

        /**
         * The display name of this rule.
         */
        public String name = "";

        /**
         * The start date for this rule.
         */
        public Date startDate = null;

        /**
         * If this rule is repeat, the end date of the repeat rule.
         */
        public Date endDate = null;

        /**
         * When repeat, whether this would end, if would, the end is todo
         */
        public boolean noEnd = true;

        /**
         * Whether the rule is repeat.
         */
        public boolean repeat = false;

        /**
         * How many times this would repeat.
         */
        public int repeatCount = -1;

        /**
         *
         */
        public int repeatPeriod = 1;

        public int repeatInterval = 1;
    }

    public static class Date {

        /**
         * Indicates whether the date is dynamic,
         * dynamic date would depend on week and vary every year
         */
        public boolean dynamic = false;

        /**
         * This field is in order to describe some date which
         * is not static but depend on the occurrence of weekday,
         * like Mother's Day (the second sunday of May).
         */
        public DynamicDate dynamicDate = null;

        /**
         * If static, the actual date(timestamp) of this excluded date.
         */
        public String date = "1234";

        public Date(boolean dynamic, DynamicDate dynamicDate, String date) {
            this.dynamic = dynamic;
            this.dynamicDate = dynamicDate;
            this.date = date;
        }
    }

    public static class DynamicDate {
        /**
         * The month in the
         * Ranging from 1 to 12, indicating from January to December.
         */
        public int month = 1;

        /**
         * The weekday content to describe a dynamic excluded date.
         * Ranging from 1 to 7, indicating from Monday to Sunday.
         */
        public int weekday = 1;

        /**
         * The nth time the weekday appear.
         * Ranging from 1 to 4, indicating the first, second, third and the fourth appearance.
         */
        public int week = 1;

        /**
         * The constructor.
         * For example, Mother's Day is on the second Sunday of May,
         * In order to describe Mother's Day, set month to 5, week to 2 and weekday to 7.
         */
        public DynamicDate(int month, int week, int weekday) {
            this.month = month;
            this.week = week;
            this.weekday = weekday;
        }
    }
}
