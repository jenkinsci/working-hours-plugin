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

import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class TimeRangeDescriptorTest {

    private TimeRange.DescriptorImpl descriptor;

    @Before
    public void setUp() {
        descriptor = new TimeRange.DescriptorImpl();
    }

    @Test
    public void testStartTimeAcceptsValidTime() {
       assertEquals(FormValidation.Kind.OK, descriptor.doCheckStartTime("01:30").kind);
    }

    @Test
    public void testStartTimeRejectsInvalidTime() {
       assertEquals(FormValidation.Kind.ERROR, descriptor.doCheckStartTime("25:30").kind);
    }

    @Test
    public void testEndTimeAcceptsValidTime() {
       assertEquals(FormValidation.Kind.OK, descriptor.doCheckEndTime("01:30").kind);
    }

    @Test
    public void testEndTimeRejectsInvalidTime() {
       assertEquals(FormValidation.Kind.ERROR, descriptor.doCheckEndTime("25:30").kind);
    }
    
    @Test
    public void testDoFillDayOfWeekItems() {
        ListBoxModel model = descriptor.doFillDayOfWeekItems();
        assertEquals(7, model.size());
    }
}
