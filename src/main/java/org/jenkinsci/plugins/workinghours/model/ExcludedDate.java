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

import de.jollyday.Holiday;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.workinghours.ValidationResult;
import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;
import org.jenkinsci.plugins.workinghours.utils.HolidayUtil;
import org.kohsuke.stapler.DataBoundConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private static final String FIELD_END_DATE = "endDate";
    private static final String FIELD_HOLIDAY_REGION = "holidayRegion";
    private static final String FIELD_HOLIDAY_ID = "holidayId";
    private static final String FIELD_NO_END = "noEnd";
    private static final String FIELD_REPEAT = "repeat";
    private static final String FIELD_REPEAT_COUNT = "repeatCount";
    private static final String FIELD_REPEAT_PERIOD = "repeatPeriod";
    private static final String FIELD_REPEAT_INTERVAL = "repeatInterval";

    /*The required fields of the class*/
    private static final String[] REQUIRED_FIELDS = {FIELD_UTC_OFFSET,
        FIELD_TIMEZONE,
        FIELD_TYPE,
        FIELD_NAME,
        FIELD_NO_END,
        FIELD_REPEAT,
        FIELD_REPEAT_COUNT,
        FIELD_REPEAT_PERIOD,
        FIELD_REPEAT_INTERVAL};
    private static final int MAX_TIME_OFFSET = 720;
    private static final int MIN_TIME_OFFSET = -720;

    /**
     * Constructs an ExcludedDate object using json.
     *
     * @param sourceJSON Json data to deserialize to an ExcludedDate object.
     */
    @DataBoundConstructor
    public ExcludedDate(JSONObject sourceJSON) {
        this.utcOffset = sourceJSON.getInt(FIELD_UTC_OFFSET);
        this.timezone = sourceJSON.getString(FIELD_TIMEZONE);
        this.startDate = new Date(sourceJSON.getJSONObject(FIELD_START_DATE), false);
        if (!sourceJSON.getJSONObject(FIELD_END_DATE).isEmpty()) {
            this.endDate = new Date(sourceJSON.getJSONObject(FIELD_END_DATE),true);
        }
        this.type = DateType.valueOf(sourceJSON.getInt(FIELD_TYPE));
        if (this.type == DateType.TYPE_HOLIDAY) {
            this.holidayId = sourceJSON.getString(FIELD_HOLIDAY_ID);
            this.holidayRegion = sourceJSON.getString(FIELD_HOLIDAY_REGION);
        }
        this.name = sourceJSON.getString(FIELD_NAME);
        this.repeat = sourceJSON.getBoolean(FIELD_REPEAT);
        this.repeatCount = sourceJSON.getInt(FIELD_REPEAT_COUNT);
        this.repeatPeriod = RepeatPeriod.valueOf(sourceJSON.getInt(FIELD_REPEAT_PERIOD));
        this.repeatInterval = sourceJSON.getInt(FIELD_REPEAT_INTERVAL);
    }

    private ExcludedDate() {

    }

    public static ValidationResult validateExcludedDate(JSONObject targetJson) {
        for (String requiredField : REQUIRED_FIELDS) {
            if (!targetJson.containsKey(requiredField)) {
                return new ValidationResult(false, requiredField, "is required");
            }
        }

        if (!(targetJson.get(FIELD_UTC_OFFSET) instanceof Number)) {
            return new ValidationResult(false, FIELD_UTC_OFFSET, "is not a number");
        } else if (targetJson.getInt(FIELD_UTC_OFFSET) > MAX_TIME_OFFSET || targetJson.getInt(FIELD_UTC_OFFSET) < MIN_TIME_OFFSET) {
            return new ValidationResult(false, FIELD_UTC_OFFSET, "should be between max:720 and min:-720");
        }

        final ValidationResult startDateValidationResult = Date.validateDate(targetJson.getJSONObject(FIELD_START_DATE),false);
        if (!startDateValidationResult.isValid()) {
            return startDateValidationResult;
        }

        if (targetJson.containsKey(FIELD_END_DATE)) {
            final ValidationResult endDateValidationResult = Date.validateDate(targetJson.getJSONObject(FIELD_END_DATE),true);
            if (!endDateValidationResult.isValid()) {
                return startDateValidationResult;
            }
        }

        return ValidationResult.getSuccessValidation();
    }

    /**
     * Judge whether today should be excluded according to this excluded date item.
     *
     * @param date {@link Calendar} Today.
     * @return {@link Boolean} Whether should be excluded.
     */
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public Boolean shouldExclude(Calendar date) {

        LocalDate checkTime = LocalDate.of(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH));

        if (this.isHoliday()) {
            /*Judge by holiday*/
            Holiday holidayThisYear = HolidayUtil.getHolidayThisYear(this.getHolidayRegion(), this.getHolidayId());

            return checkTime.equals(LocalDate.of(
                holidayThisYear.getDate().getYear(),
                holidayThisYear.getDate().getMonthOfYear() + 1,
                holidayThisYear.getDate().getDayOfMonth()));
        } else if (this.startDate.isDynamic()) {
            /*Judge by dynamic date */
            final Date startDate = this.getStartDate();
            switch (this.getRepeatPeriod()) {
                case REPEAT_BY_WEEK:
                    return checkTime.getDayOfWeek().getValue() == startDate.getDynamicWeekday();
                case REPEAT_BY_MONTH:
                    return DynamicDateUtil.nextOccurrenceByMonth(startDate.getDynamicWeek(), startDate.getDynamicWeekday(),checkTime).isEqual(checkTime);
                case REPEAT_BY_YEAR:
                    return DynamicDateUtil.nextOccurrenceByYear(startDate.getDynamicMonth(), startDate.getDynamicWeek(), startDate.getDynamicWeekday(),checkTime).isEqual(checkTime);
                default:
                    return false;
            }
        } else {
            /*Judge by static date */
            return this.getStartDate().getLocalDate().isEqual(checkTime);
        }
    }

    private boolean isHoliday() {
        return this.type == DateType.TYPE_HOLIDAY;
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
    private DateType type = DateType.TYPE_CUSTOM;

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
     * Id of the selected holiday.
     */
    private String holidayId;

    /**
     * Region Code of which the holiday belong to.
     */
    private String holidayRegion;

    /**
     *
     */
    private RepeatPeriod repeatPeriod;

    private int repeatInterval = 1;

    public int getUtcOffset() {
        return utcOffset;
    }

    public int getType() {
        return type.getValue();
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

    public RepeatPeriod getRepeatPeriod() {
        return repeatPeriod;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getHolidayRegion() {
        return holidayRegion;
    }

    public void setHolidayRegion(String holidayRegion) {
        this.holidayRegion = holidayRegion;
    }

    public String getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    public static class Date {

        private static final String FIELD_DYNAMIC = "dynamic";
        private static final String FIELD_DATE = "date";
        private static final String FIELD_DYNAMIC_MONTH = "dynamicMonth";
        private static final String FIELD_DYNAMIC_WEEK = "dynamicWeek";
        private static final String FIELD_DYNAMIC_WEEKDAY = "dynamicWeekday";

        private static final String[] REQUIRED_FIELDS_FOR_DYNAMIC = {FIELD_DYNAMIC_MONTH, FIELD_DYNAMIC_WEEK, FIELD_DYNAMIC_WEEKDAY};

        public Date(JSONObject jsonObject, boolean isEnd) {
            this.date = jsonObject.getString(FIELD_DATE);
            if(isEnd){
                return;
            }
            this.dynamic = jsonObject.getBoolean(FIELD_DYNAMIC);
            this.dynamicMonth = jsonObject.getInt(FIELD_DYNAMIC_MONTH);
            this.dynamicWeek = jsonObject.getInt(FIELD_DYNAMIC_WEEK);
            this.dynamicWeekday = jsonObject.getInt(FIELD_DYNAMIC_WEEKDAY);
        }

        LocalDate getLocalDate() {
            return LocalDate.parse(this.getDate(), DateTimeFormatter.ISO_DATE_TIME);
        }

        public boolean isDynamic() {
            return dynamic;
        }

        static ValidationResult validateDate(JSONObject targetObject, boolean isEndDate) {
            if (!targetObject.containsKey(FIELD_DYNAMIC)) {
                return new ValidationResult(false, FIELD_DYNAMIC, "is required");
            } else {
                if(isEndDate){
                    return ValidationResult.getSuccessValidation();
                }
                if (!(targetObject.get(FIELD_DYNAMIC) instanceof Boolean)) {
                    return new ValidationResult(false, FIELD_DYNAMIC, "should be bool");
                } else {
                    boolean dynamic = targetObject.getBoolean(FIELD_DYNAMIC);
                    if (dynamic) {
                        for (String field : REQUIRED_FIELDS_FOR_DYNAMIC) {
                            if (!targetObject.containsKey(field)) {
                                return new ValidationResult(false, field, "is required");
                            }
                        }
                        if (!(targetObject.get(FIELD_DYNAMIC_WEEKDAY) instanceof Integer)) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEKDAY, "should be int");
                        } else if (targetObject.getInt(FIELD_DYNAMIC_WEEKDAY) > 7 || targetObject.getInt(FIELD_DYNAMIC_WEEKDAY) < 1) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEKDAY, "should be between 1 and 7");
                        }

                        if (!(targetObject.get(FIELD_DYNAMIC_WEEK) instanceof Integer)) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEK, "should be int");
                        } else if (targetObject.getInt(FIELD_DYNAMIC_WEEK) > 4 || targetObject.getInt(FIELD_DYNAMIC_WEEK) < 1) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEK, "should be between 1 and 4");
                        }

                        if (!(targetObject.get(FIELD_DYNAMIC_MONTH) instanceof Integer)) {
                            return new ValidationResult(false, FIELD_DYNAMIC_MONTH, "should be int");
                        } else if (targetObject.getInt(FIELD_DYNAMIC_MONTH) > 12 || targetObject.getInt(FIELD_DYNAMIC_MONTH) < 1) {
                            return new ValidationResult(false, FIELD_DYNAMIC_MONTH, "should be between 1 and 12");
                        }
                    } else {
                        if (!targetObject.containsKey(FIELD_DATE)) {
                            return new ValidationResult(false, FIELD_DATE, "is required");
                        } else if (!(targetObject.get(FIELD_DATE) instanceof String)) {
                            return new ValidationResult(false, FIELD_DATE, "should be string");
                        }
                    }
                }
                return ValidationResult.getSuccessValidation();
            }
        }

        /**
         * Indicates whether the date is dynamic,
         * dynamic date would depend on week and vary every year
         */
        private boolean dynamic;

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
