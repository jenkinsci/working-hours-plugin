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
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.util.Calendar;

/**
 * Encapsulates an excluded date along with name for UI purposes.
 *
 * @author jxpearce@godaddy.com
 */
public class ExcludedDate {

    private static final String FIELD_UTC_OFFSET = "utcOffset";
    private static final String FIELD_TIMEZONE = "timezone";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_START_DATE = "startDate";
    private static final String FIELD_END_DATE = "endData";
    private static final String FIELD_NO_END = "noENd";
    private static final String FIELD_REPEAT = "repeat";
    private static final String FIELD_REPEAT_COUNT = "repeatCount";
    private static final String FIELD_REPEAT_PERIOD = "repeatPeriod";
    private static final String FIELD_REPEAT_INTERVAL = "repeatInterval";


    /**
     * Constructs an ExcludedDate object using json.
     *
     * @param sourceJSON Json data to deserialize to an ExcludedDate object.
     */
    @DataBoundConstructor
    public ExcludedDate(JSONObject sourceJSON) {
        this.utcOffset = sourceJSON.getInt(FIELD_UTC_OFFSET);
        this.timezone = sourceJSON.getString(FIELD_TIMEZONE);
        this.startDate = new Date(sourceJSON.getJSONObject(FIELD_START_DATE));
        if (!sourceJSON.getJSONObject(FIELD_END_DATE).isEmpty()) {
            this.endDate = new Date(sourceJSON.getJSONObject(FIELD_END_DATE));
        }
        this.name = sourceJSON.getString(FIELD_NAME);
        this.repeat = sourceJSON.getBoolean(FIELD_REPEAT);
        this.repeatCount = sourceJSON.getInt(FIELD_REPEAT_COUNT);
        this.repeatPeriod = sourceJSON.getInt(FIELD_REPEAT_PERIOD);
        this.repeatInterval = sourceJSON.getInt(FIELD_REPEAT_INTERVAL);
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

    /**
     * Minutes offset to the UTC time, indicates the base timezone of the excluded date.
     * Default to UTC(UTC+0).
     */
    private int utcOffset = 0;

    /**
     * Name of the selected timezone.
     */
    private String timezone;

    /**
     * A type that indicates whether
     */
    private String type = DateType.TYPE_GREGORIAN;

    /**
     * The display name of this rule.
     */
    private String name = "";

    /**
     * The start date for this rule.
     */
    private Date startDate = null;

    /**
     * If this rule is repeat, the end date of the repeat rule.
     */
    private Date endDate = null;

    /**
     * When repeat, whether this would end, if would, the end is the field "end date"
     */
    private boolean noEnd = true;

    /**
     * Whether the rule is repeat.
     */
    private boolean repeat = false;

    /**
     * How many times this would repeat.
     */
    private int repeatCount = -1;

    /**
     *
     */
    private int repeatPeriod = 1;

    private int repeatInterval = 1;

    public int getUtcOffset() {
        return utcOffset;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isNoEnd() {
        return noEnd;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getRepeatPeriod() {
        return repeatPeriod;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public String getTimezone() {
        return timezone;
    }

    public static class Date {

        private static final String FIELD_DYNAMIC = "dynamic";
        private static final String FIELD_DATE = "date";
        private static final String FIELD_DYNAMIC_MONTH = "dynamicMonth";
        private static final String FIELD_DYNAMIC_WEEK = "dynamicWeek";
        private static final String FIELD_DYNAMIC_WEEKDAY = "dynamicWeekday";

        public Date(JSONObject jsonObject) {
            this.dynamic = jsonObject.getBoolean(FIELD_DYNAMIC);
            this.date = jsonObject.getString(FIELD_DATE);
            this.dynamicMonth = jsonObject.getInt(FIELD_DYNAMIC_MONTH);
            this.dynamicWeek = jsonObject.getInt(FIELD_DYNAMIC_WEEK);
            this.dynamicWeekday = jsonObject.getInt(FIELD_DYNAMIC_WEEKDAY);
        }

        public boolean isDynamic() {
            return dynamic;
        }

        /**
         * Indicates whether the date is dynamic,
         * dynamic date would depend on week and vary every year
         */
        private boolean dynamic;

        /**
         * This field is in order to describe some date which
         * is not static but depend on the occurrence of weekday,
         * like Mother's Day (the second sunday of May).
         */

        /**
         * If static, the actual date(timestamp) of this excluded date.
         */
        private String date;


        public String getDate() {
            return date;
        }

        /**
         * The month in the year
         * Ranging from 1 to 12, indicating from January to December.
         */
        private int dynamicMonth;

        /**
         * The weekday content to describe a dynamic excluded date.
         * Ranging from 1 to 7, indicating from Monday to Sunday.
         */
        private int dynamicWeekday;

        /**
         * The nth time the weekday appear.
         * Ranging from 1 to 4, indicating the first, second, third and the fourth appearance.
         */
        private int dynamicWeek;

        public int getDynamicMonth() {
            return dynamicMonth;
        }

        public int getDynamicWeekday() {
            return dynamicWeekday;
        }

        public int getDynamicWeek() {
            return dynamicWeek;
        }
    }
}
