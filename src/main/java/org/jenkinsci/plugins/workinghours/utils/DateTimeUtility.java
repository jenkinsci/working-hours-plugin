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
package org.jenkinsci.plugins.workinghours.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Provides common methods to validate and convert date/time strings in the
 * specific formats supported by the plugin.
 *
 * @author jxpearce@godaddy.com
 */
public final class DateTimeUtility {
    /**
     * Private constructor to prevent creating instances of this class.
     */
    private DateTimeUtility() {
    }

    /**
     * Convert a time string to LocalTime.
     *
     * @param value value to convert.
     * @return LocalTime, or null if not valid.
     */
    public static LocalTime localTime(final String value) {
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HHmm");
            return LocalTime.parse(value, sdf);
        } catch (DateTimeParseException ex) {
            System.out.println("Could not parse into format HHmm");
        }
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("H:m");
            return LocalTime.parse(value, sdf);
        } catch (DateTimeParseException ex) {
            System.out.println("Could not parse into format H:m");
        }
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("h:m a");
            return LocalTime.parse(value, sdf);
        } catch (DateTimeParseException ex) {
            System.out.println("Could not parse into format h:m a");
        }

        return null;
    }

    public static LocalTime localTimeFromMinutes(final int minutes) {
        return LocalTime.of(minutes / 60, minutes % 60);
    }

    /**
     * Checks whether a string is a valid time string.
     *
     * @param value value to check.
     * @return true if valid; false otherwise.
     */
    public static Boolean isValidTime(final String value) {
        return null != localTime(value);
    }

    /**
     * Convert a date string to LocalDate.
     *
     * @param value value to convert.
     * @return LocalDates, or null if not valid.
     */
    public static LocalDate localDate(final String value) {
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M/d/y");
            return LocalDate.parse(value, sdf);
        } catch (DateTimeParseException ex) {
        }
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("M-d-y");
            return LocalDate.parse(value, sdf);
        } catch (DateTimeParseException ex) {
        }
        return null;
    }

    /**
     * Checks whether a string is a valid date string.
     *
     * @param value value to check.
     * @return true if valid; false otherwise.
     */
    public static Boolean isValidDate(final String value) {
        return null != localDate(value);
    }
}
