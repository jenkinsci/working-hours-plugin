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

import java.util.Calendar;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeRangeTest {

    @Test
    public void testGetStartTime() {
        TimeRange instance = new TimeRange();
        String expResult = "";
        String result = instance.getStartTime();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetStartTime() {
        String startTime = "";
        TimeRange instance = new TimeRange();
        instance.setStartTime(startTime);
        assertEquals(startTime, instance.getStartTime());
    }

    @Test
    public void testGetEndTime() {
        TimeRange instance = new TimeRange();
        String expResult = "";
        String result = instance.getEndTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEndTime method, of class JobTimeRange.
     */
    @Test
    public void testSetEndTime() {
        System.out.println("setEndTime");
        String endTime = "";
        TimeRange instance = new TimeRange();
        instance.setEndTime(endTime);
        assertEquals(endTime, instance.getEndTime());
    }

    @Test
    public void testSetDayOfWeek() {
        System.out.println("setDayOfWeek");
        int dayOfWeek = 4;
        TimeRange instance = new TimeRange();
        instance.setDayOfWeek(dayOfWeek);
        assertEquals(dayOfWeek, instance.getDayOfWeek());
    }

    @Test
    public void testIncludesTime() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.THURSDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 10, 30);

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testIncludesLowerBound() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.THURSDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 10, 00);

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testIncludesUpperBound() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.THURSDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 11, 00);

        assertTrue(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesTimeBefore() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.THURSDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 9, 59);

        assertFalse(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesTimeAfter() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.THURSDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 11, 1);

        assertFalse(instance.includesTime(testValue));
    }

    @Test
    public void testExcludesIfDayDifferent() {
        TimeRange instance = new TimeRange("10:00 AM", "11:00 AM", Calendar.WEDNESDAY);

        Calendar testValue = Calendar.getInstance();
        // Jan 1, 2018 is a Thursday
        testValue.set(2018, 0, 18, 10, 30);

        assertFalse(instance.includesTime(testValue));
    }
}
