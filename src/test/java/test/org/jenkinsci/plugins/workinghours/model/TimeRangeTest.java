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
package test.org.jenkinsci.plugins.workinghours.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeRangeTest {

    @Test
    public void testIncludesTime() {
        TimeRange instance = new TimeRange(8*60,18*60, DayOfWeek.THURSDAY);

        LocalDateTime testValue = LocalDateTime.of(2018,1,18,10,30);
        // Jan 18, 2018 is a Thursday

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testIncludesLowerBound() {
        TimeRange instance = new TimeRange(8*60, 18*60, DayOfWeek.THURSDAY);

        LocalDateTime testValue = LocalDateTime.of(2018,1,18,8,0);
        // Jan 18, 2018 is a Thursday

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testIncludesUpperBound() {
        TimeRange instance = new TimeRange(8*60, 18*60, DayOfWeek.THURSDAY);

        // Jan 18, 2018 is a Thursday
        LocalDateTime testValue = LocalDateTime.of(2018, 1, 18, 18, 0);

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesTimeBefore() {
        TimeRange instance = new TimeRange(8*60, 18*60, DayOfWeek.THURSDAY);

        // Jan 18, 2018 is a Thursday
        LocalDateTime testValue = LocalDateTime.of(2018, 1, 18, 7, 59);

        assertFalse(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesTimeAfter() {
        TimeRange instance =  new TimeRange(8*60, 18*60, DayOfWeek.THURSDAY);

        // Jan 18, 2018 is a Thursday
        LocalDateTime testValue = LocalDateTime.of(2018, 1, 18, 18, 1);

        assertFalse(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesIfDayDifferent() {
        TimeRange instance =  new TimeRange(8*60, 18*60, DayOfWeek.FRIDAY);

        // Jan 18, 2018 is a Thursday
        LocalDateTime testValue = LocalDateTime.of(2018, 1, 18, 10, 30);

        assertFalse(instance.includesTime(testValue));
    }
}
