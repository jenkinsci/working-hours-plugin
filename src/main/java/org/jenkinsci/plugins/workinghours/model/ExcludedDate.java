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
import org.jenkinsci.plugins.workinghours.presets.PresetManager;
import org.jenkinsci.plugins.workinghours.utils.DateTimeUtility;
import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;
import org.jenkinsci.plugins.workinghours.utils.JollydayUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

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
    private static final String FIELD_HOLIDAY_KEY = "key";
    private static final String FIELD_HOLIDAY = "holiday";
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

    public static final int REPEAT_NO_END = -1;

    /**
     * Constructs an ExcludedDate object using json.
     *
     * @param sourceJSON Json data to deserialize to an ExcludedDate object.
     */
    public ExcludedDate(JSONObject sourceJSON) {
        this.utcOffset = sourceJSON.getInt(FIELD_UTC_OFFSET);
        this.timezone = sourceJSON.getString(FIELD_TIMEZONE);
        this.startDate = new Date(sourceJSON.getJSONObject(FIELD_START_DATE), false);
        if (!sourceJSON.getJSONObject(FIELD_END_DATE).isEmpty()) {
            this.endDate = new Date(sourceJSON.getJSONObject(FIELD_END_DATE), true);
        }
        this.type = DateType.valueOf(sourceJSON.getInt(FIELD_TYPE));
        if (this.type == DateType.TYPE_HOLIDAY) {
            JSONObject holiday = sourceJSON.getJSONObject(FIELD_HOLIDAY);
            this.holidayId = holiday.getString(FIELD_HOLIDAY_KEY);
            this.holidayRegion = sourceJSON.getString(FIELD_HOLIDAY_REGION);
        }
        this.name = sourceJSON.getString(FIELD_NAME);
        this.repeat = sourceJSON.getBoolean(FIELD_REPEAT);
        this.repeatCount = sourceJSON.getInt(FIELD_REPEAT_COUNT);
        this.repeatPeriod = RepeatPeriod.valueOf(sourceJSON.getInt(FIELD_REPEAT_PERIOD));
        this.repeatInterval = sourceJSON.getInt(FIELD_REPEAT_INTERVAL);

        this.initializeFirstOccurrence(null);
    }

    public ExcludedDate(int utcOffset, String timezone, DateType type, String name, Date startDate, Date endDate, boolean noEnd, boolean repeat, int repeatCount, String holidayId, String holidayRegion, RepeatPeriod repeatPeriod, int repeatInterval) {
        this.utcOffset = utcOffset;
        this.timezone = timezone;
        this.type = type;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.noEnd = noEnd;
        this.repeat = repeat;
        this.repeatCount = repeatCount;
        this.holidayId = holidayId;
        this.holidayRegion = holidayRegion;
        this.repeatPeriod = repeatPeriod;
        this.repeatInterval = repeatInterval;
    }

    /**
     * Disable the default constructor, use {@link ExcludedDate.Builder}
     */
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

        final ValidationResult startDateValidationResult = Date.validateDate(targetJson.getJSONObject(FIELD_START_DATE), false);
        if (!startDateValidationResult.isValid()) {
            return startDateValidationResult;
        }

        if (targetJson.containsKey(FIELD_END_DATE)) {
            final ValidationResult endDateValidationResult = Date.validateDate(targetJson.getJSONObject(FIELD_END_DATE), true);
            if (!endDateValidationResult.isValid()) {
                return startDateValidationResult;
            }
        }

        return ValidationResult.getSuccessValidation();
    }

    public boolean shouldExclude(LocalDate checkDate) {
        return innerShouldExclude(checkDate);
    }

    public boolean shouldExclude() {
        return innerShouldExclude(ZonedDateTime.now(ZoneId.of(this.getTimezone())).toLocalDate());
    }

    /**
     * Judge whether today should be excluded according to this excluded date item.
     *
     * @param checkDate The date to check, if null, the field timezone would be used to get a
     *                  {@link ZonedDateTime} to get a localDate.
     * @return {@link Boolean} Whether now should be excluded.
     */
    private boolean innerShouldExclude(LocalDate checkDate) {
        //If ends, exclude the date if it's after the end date.
        if (!this.isNoEnd()) {
            if (checkDate.isAfter(this.getEndDate().getLocalDate())) {
                return false;
            }
        }

        if (this.isHoliday()) {
            /*Judge by holiday*/
            Holiday holidayThisYear = JollydayUtil.getHolidayThisYear(this.getHolidayRegion(), this.getHolidayId());
            return checkDate.equals(DateTimeUtility.jodaDateToLocalDate(holidayThisYear.getDate()));
        } else if (this.startDate.isDynamic()) {
            /*Judge by dynamic date */
            final Date startDate = this.getStartDate();
            switch (this.getRepeatPeriodEnum()) {
                case REPEAT_BY_WEEK:
                    return DynamicDateUtil.nextOccurrenceByWeek(startDate.getDynamicWeekday(), checkDate).isEqual(checkDate)
                        && judgeRepeatIntervalAndCount(getTwoDaysDistanceInWeeks(startDate.getFirstOccurrence(), checkDate));
                case REPEAT_BY_MONTH:
                    return DynamicDateUtil.nextOccurrenceByMonth(startDate.getDynamicWeek(), startDate.getDynamicWeekday(), checkDate).isEqual(checkDate)
                        && judgeRepeatIntervalAndCount(getTwoDaysDistanceInMonths(startDate.getFirstOccurrence(), checkDate));
                case REPEAT_BY_YEAR:
                    return DynamicDateUtil.nextOccurrenceByYear(startDate.getDynamicMonth(), startDate.getDynamicWeek(), startDate.getDynamicWeekday(), checkDate).isEqual(checkDate)
                        && judgeRepeatIntervalAndCount(getTwoDaysDistanceInYears(startDate.getFirstOccurrence(), checkDate));
                default:
                    return false;
            }
        } else {
            /*Judge by static date */
            if (this.isRepeat()) {
                return this.judgeRepeatWithStaticDate(checkDate, this.getStartDate().getLocalDate());
            }
            return this.getStartDate().getLocalDate().isEqual(checkDate);
        }
    }

    /**
     * Check whether a date should be excluded according to the repeat interval/period/count rule.
     *
     * @param checkDate  The date to check, typically would be LocalDate.now()
     * @param targetDate The date which has been set to judge a date.
     * @return Whether the check date should be excluded.
     */
    private boolean judgeRepeatWithStaticDate(LocalDate checkDate, LocalDate targetDate) {
        switch (this.getRepeatPeriodEnum()) {
            case REPEAT_BY_WEEK:
                return checkDate.getDayOfWeek().getValue() == targetDate.getDayOfWeek().getValue()
                    && judgeRepeatIntervalAndCount(getTwoDaysDistanceInWeeks(targetDate, checkDate));
            case REPEAT_BY_MONTH:
                return checkDate.getDayOfMonth() == targetDate.getDayOfMonth()
                    && judgeRepeatIntervalAndCount(getTwoDaysDistanceInMonths(targetDate, checkDate));
            case REPEAT_BY_YEAR:
                return checkDate.getDayOfMonth() == targetDate.getDayOfMonth()
                    && checkDate.getMonth() == targetDate.getMonth()
                    && judgeRepeatIntervalAndCount(getTwoDaysDistanceInYears(targetDate, checkDate));
            default:
                return false;
        }
    }

    /**
     * Judge this occurrence is hit the repeat interval and count rule.
     *
     * @param appearTimes The times the excluded date has occurred.
     * @return Whether this occurrence is hit by the rule.
     */
    private boolean judgeRepeatIntervalAndCount(long appearTimes) {
        return appearTimes % getRepeatInterval() == 0 && (getRepeatCount() == REPEAT_NO_END || (getRepeatCount() > 0 && appearTimes / getRepeatInterval() <= getRepeatCount()));
    }

    private long getTwoDaysDistanceInDays(LocalDate earlyDate, LocalDate laterDate) {
        return laterDate.getLong(ChronoField.EPOCH_DAY) - earlyDate.getLong(ChronoField.EPOCH_DAY);
    }

    private long getTwoDaysDistanceInWeeks(LocalDate earlyDate, LocalDate laterDate) {
        return (laterDate.getLong(ChronoField.EPOCH_DAY) - earlyDate.getLong(ChronoField.EPOCH_DAY)) / 7;
    }

    private long getTwoDaysDistanceInMonths(LocalDate earlyDate, LocalDate laterDate) {
        return laterDate.getLong(ChronoField.PROLEPTIC_MONTH) - earlyDate.getLong(ChronoField.PROLEPTIC_MONTH);
    }

    private long getTwoDaysDistanceInYears(LocalDate earlyDate, LocalDate laterDate) {
        return laterDate.getLong(ChronoField.YEAR) - earlyDate.getLong(ChronoField.YEAR);
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
        return this.noEnd;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getRepeatPeriod() {
        return repeatPeriod.getValue();
    }

    public RepeatPeriod getRepeatPeriodEnum() {
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

    public org.jenkinsci.plugins.workinghours.model.Holiday getHoliday() {
        if (this.getHolidayId() != null) {
            return PresetManager.getInstance().getCertainHolidayThisYear(this.getHolidayRegion(), this.getHolidayId());
        } else {
            return null;
        }
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

        public Date(JSONObject jsonObject, boolean isEndDate) {
            this.date = jsonObject.getString(FIELD_DATE);
            //The end date is static.
            if (isEndDate) {
                return;
            }
            this.dynamic = jsonObject.getBoolean(FIELD_DYNAMIC);
            this.dynamicMonth = jsonObject.getInt(FIELD_DYNAMIC_MONTH);
            this.dynamicWeek = jsonObject.getInt(FIELD_DYNAMIC_WEEK);
            this.dynamicWeekday = jsonObject.getInt(FIELD_DYNAMIC_WEEKDAY);
        }

        public Date() {

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
                if (isEndDate) {
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
                        } else if (targetObject.getInt(FIELD_DYNAMIC_WEEKDAY) > DayOfWeek.SUNDAY.getValue() || targetObject.getInt(FIELD_DYNAMIC_WEEKDAY) < DayOfWeek.MONDAY.getValue()) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEKDAY, "should be between 1 and 7");
                        }

                        if (!(targetObject.get(FIELD_DYNAMIC_WEEK) instanceof Integer)) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEK, "should be int");
                        } else if (targetObject.getInt(FIELD_DYNAMIC_WEEK) > 4 || targetObject.getInt(FIELD_DYNAMIC_WEEK) < 1) {
                            return new ValidationResult(false, FIELD_DYNAMIC_WEEK, "should be between 1 and 4");
                        }

                        if (!(targetObject.get(FIELD_DYNAMIC_MONTH) instanceof Integer)) {
                            return new ValidationResult(false, FIELD_DYNAMIC_MONTH, "should be int");
                        } else if (targetObject.getInt(FIELD_DYNAMIC_MONTH) > Month.DECEMBER.getValue() || targetObject.getInt(FIELD_DYNAMIC_MONTH) < Month.JANUARY.getValue()) {
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

        private LocalDate firstOccurrence;

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

        public void setDynamic(boolean dynamic) {
            this.dynamic = dynamic;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDynamicMonth(int dynamicMonth) {
            this.dynamicMonth = dynamicMonth;
        }

        public void setDynamicWeekday(int dynamicWeekday) {
            this.dynamicWeekday = dynamicWeekday;
        }

        public void setDynamicWeek(int dynamicWeek) {
            this.dynamicWeek = dynamicWeek;
        }

        public static Date fromLocalDate(LocalDate date) {
            Date newDate = new Date();
            newDate.setDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.of(date, LocalTime.of(0, 0))));
            newDate.setDynamic(false);
            return newDate;
        }

        public LocalDate getFirstOccurrence() {
            return firstOccurrence;
        }

        public void setFirstOccurrence(LocalDate firstOccurrence) {
            this.firstOccurrence = firstOccurrence;
        }
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setType(DateType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNoEnd(boolean noEnd) {
        this.noEnd = noEnd;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setRepeatPeriod(RepeatPeriod repeatPeriod) {
        this.repeatPeriod = repeatPeriod;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }


    public static final class Builder {
        private ExcludedDate excludedDate;

        private Builder() {
            excludedDate = new ExcludedDate();
        }

        public static Builder anExcludedDate() {
            return new Builder();
        }

        public Builder withUtcOffset(int utcOffset) {
            excludedDate.setUtcOffset(utcOffset);
            return this;
        }

        public Builder withTimezone(String timezone) {
            excludedDate.setTimezone(timezone);
            return this;
        }

        public Builder withType(DateType type) {
            excludedDate.setType(type);
            return this;
        }

        public Builder withName(String name) {
            excludedDate.setName(name);
            return this;
        }

        public Builder withStartDate(Date startDate) {
            excludedDate.setStartDate(startDate);
            return this;
        }

        public Builder withEndDate(Date endDate) {
            excludedDate.setEndDate(endDate);
            return this;
        }

        public Builder withNoEnd(boolean noEnd) {
            excludedDate.setNoEnd(noEnd);
            return this;
        }

        public Builder withRepeat(boolean repeat) {
            excludedDate.setRepeat(repeat);
            return this;
        }

        public Builder withRepeatCount(int repeatCount) {
            excludedDate.setRepeatCount(repeatCount);
            return this;
        }

        public Builder withHolidayId(String holidayId) {
            excludedDate.setHolidayId(holidayId);
            return this;
        }

        public Builder withHolidayRegion(String holidayRegion) {
            excludedDate.setHolidayRegion(holidayRegion);
            return this;
        }

        public Builder withRepeatPeriod(RepeatPeriod repeatPeriod) {
            excludedDate.setRepeatPeriod(repeatPeriod);
            return this;
        }

        public Builder withRepeatInterval(int repeatInterval) {
            excludedDate.setRepeatInterval(repeatInterval);
            return this;
        }

        public ExcludedDate build() {
            excludedDate.initializeFirstOccurrence(null);
            return excludedDate;
        }
    }

    public void initializeFirstOccurrence(LocalDate now) {
        //If dynamic, initialize the first occurrence of the data.
        if (this.startDate != null && this.startDate.isDynamic()) {
            switch (this.getRepeatPeriodEnum()) {
                case REPEAT_BY_WEEK:
                    this.getStartDate().setFirstOccurrence(DynamicDateUtil.nextOccurrenceByWeek(this.getStartDate().getDynamicWeekday(), now));
                    break;
                case REPEAT_BY_MONTH:
                    this.getStartDate().setFirstOccurrence(DynamicDateUtil.nextOccurrenceByMonth(this.getStartDate().getDynamicWeek(), this.getStartDate().getDynamicWeekday(), now));
                    break;
                case REPEAT_BY_YEAR:
                    this.getStartDate().setFirstOccurrence(DynamicDateUtil.nextOccurrenceByYear(this.getStartDate().getDynamicMonth(), this.getStartDate().getDynamicWeek(), this.getStartDate().getDynamicWeekday(), now));
                    break;
                default:
                    return;
            }
        }
    }
}
