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

import net.sf.json.JSONObject;
import org.jenkinsci.plugins.workinghours.ValidationResult;
import org.jenkinsci.plugins.workinghours.utils.DateTimeUtility;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * Encapsulates a time range, which matches a times on a particular day of the
 * week that occur between a start and end time.
 *
 * @author jxpearce@godaddy.com
 */
public class TimeRange {

    private static final String FIELD_START_TIME = "startTime";
    private static final String FIELD_END_TIME = "endTime";
    private static final String FIELD_DAY_OF_WEEK = "dayOfWeek";

    private static final int MAX_MINUTES = 1440;
    private static final int MAX_MINUTES_MINUS_ONE = 1439;
    private static final int MIN_MINUTES = 0;

    public static ValidationResult validateTimeRange(JSONObject targetJson) {
        if (!(targetJson.containsKey(FIELD_START_TIME))) {
            return new ValidationResult(false, FIELD_START_TIME, "is needed");
        } else if (!(targetJson.get(FIELD_START_TIME) instanceof Number)) {
            return new ValidationResult(false, FIELD_START_TIME, "is not a number");
        } else if (targetJson.getInt(FIELD_START_TIME) > MAX_MINUTES || targetJson.getInt(FIELD_START_TIME) < MIN_MINUTES) {
            return new ValidationResult(false, FIELD_START_TIME, "should be between 0 and 1440");
        }

        if (!(targetJson.containsKey(FIELD_END_TIME))) {
            return new ValidationResult(false, FIELD_END_TIME, "is needed");
        } else if (!(targetJson.get(FIELD_END_TIME) instanceof Number)) {
            return new ValidationResult(false, FIELD_END_TIME, "is not a number");
        } else if (targetJson.getInt(FIELD_END_TIME) > MAX_MINUTES || targetJson.getInt(FIELD_END_TIME) < MIN_MINUTES) {
            return new ValidationResult(false, FIELD_END_TIME, "should be between 0 and 1440");
        } else if (targetJson.getInt(FIELD_END_TIME) < targetJson.getInt(FIELD_START_TIME)) {
            return new ValidationResult(false, FIELD_END_TIME, "should be after start time");
        }

        if (!(targetJson.containsKey(FIELD_DAY_OF_WEEK))) {
            return new ValidationResult(false, FIELD_DAY_OF_WEEK, "is needed");
           } else if (!(targetJson.get(FIELD_DAY_OF_WEEK) instanceof Number)) {
            return new ValidationResult(false, FIELD_DAY_OF_WEEK, "is not a number");
        } else if (targetJson.getInt(FIELD_DAY_OF_WEEK) > DayOfWeek.SUNDAY.getValue()  || targetJson.getInt(FIELD_DAY_OF_WEEK) < DayOfWeek.MONDAY.getValue()) {
            return new ValidationResult(false, FIELD_DAY_OF_WEEK, "should be between 1 and 7");
        }

        return ValidationResult.getSuccessValidation();
    }


    /**
     * Constructs a TimeRange object.
     *
     * @param startTime The startTime of the time range.
     * @param endTime   The endTime of the time range.
     * @param dayOfWeek The day of the time range.
     */
    public TimeRange(int startTime, int endTime, DayOfWeek dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Constructs a TimeRange object using JSON.
     *
     * @param sourceJSON The source json data that contains the fields.
     */
    public TimeRange(JSONObject sourceJSON) {
        this.startTime = sourceJSON.getInt(FIELD_START_TIME);
        this.endTime = sourceJSON.getInt(FIELD_END_TIME);
        this.dayOfWeek = DayOfWeek.of(sourceJSON.getInt(FIELD_DAY_OF_WEEK));
    }


    /**
     * Check whether configured rule includes a date.
     *
     * @param checkTime Time to check
     * @return true if date is inside of configured rule.
     */
    public Boolean includesTime(LocalDateTime checkTime) {
        LocalTime allowedStartTime = DateTimeUtility.localTimeFromMinutes(getStartTime());
        LocalTime allowedEndTime = DateTimeUtility.localTimeFromMinutes(getEndTime());

        return checkTime.getDayOfWeek().getValue() == this.getDayOfWeek()
            && (checkTime.toLocalTime().equals(allowedStartTime)
            || checkTime.toLocalTime().isAfter(allowedStartTime))
            && (checkTime.toLocalTime().equals(allowedEndTime)
            || checkTime.toLocalTime().isBefore(allowedEndTime));
    }


    /*The start time of the time range, in form of the number of the minutes from 00:00*/
    private int startTime = 0;

    /*The end time of the time range, in form of the number of the minutes from 00:00*/
    private int endTime = 0;

    /*The day of week*/
    private DayOfWeek dayOfWeek;

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek.getValue();
    }
}
