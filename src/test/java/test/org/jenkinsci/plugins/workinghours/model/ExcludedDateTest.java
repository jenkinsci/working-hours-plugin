/*
 * The MIT License
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

import hudson.util.FormValidation;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExcludedDateTest {
    
    public ExcludedDateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Verify name is set in constructor.
     */
    @Test
    public void testGetName() {
        String testName = "test name";
        ExcludedDate instance = new ExcludedDate(testName, "01-01-2017", false);
        assertEquals(testName, instance.getName());
    }

    /**
     * Verify set/get name.
     */
    @Test
    public void testGetSetName() {
        String testName = "test name";
        ExcludedDate instance = new ExcludedDate("", "", false);
        instance.setName(testName);
        assertEquals(testName, instance.getName());
    }

    /**
     * Verify date is set in constructor.
     */
    @Test
    public void testGetDate() {
        String testName = "test name";
        String testDate = "01-01-2017";
        ExcludedDate instance = new ExcludedDate(testName, testDate, false);
        assertEquals(testDate, instance.getDate());
    }

    /**
     * Verify set/get date.
     */
    @Test
    public void testGetSetDate() {
        String testDate = "01-01-2017";
        ExcludedDate instance = new ExcludedDate("", "", false);
        instance.setDate(testDate);
        assertEquals(testDate, instance.getDate());
    }

    /**
     * Verify repeatYearly is set in constructor.
     */
    @Test
    public void testIsRepeatYearly() {
        String testName = "test name";
        String testDate = "01-01-2017";
        boolean testRepeatYearly = true;
        ExcludedDate instance = new ExcludedDate(testName, testDate, testRepeatYearly);
        assertEquals(testRepeatYearly, instance.isRepeatYearly());
    }

    /**
     * Verify set/is repeatYearly.
     */
    @Test
    public void testIsSetRepeatYearly() {
        boolean testRepeatYearly = true;
        ExcludedDate instance = new ExcludedDate("", "", false);
        instance.setRepeatYearly(testRepeatYearly);
        assertEquals(testRepeatYearly, instance.isRepeatYearly());
    }
    
    /**
     * Verify doCheckDate with empty string returns OK.
     */
    @Test
    public void testDoCheckDateEmpty() {
        ExcludedDate.DescriptorImpl descriptor = 
                new ExcludedDate.DescriptorImpl();
        assertEquals(FormValidation.Kind.OK, descriptor.doCheckDate("").kind);
    }

    /**
     * Verify doCheckDate with valid date returns OK.
     */
    @Test
    public void testDoCheckDateValid() {
        ExcludedDate.DescriptorImpl descriptor = 
                new ExcludedDate.DescriptorImpl();
        assertEquals(FormValidation.Kind.OK, 
                descriptor.doCheckDate("01-01-2112").kind);
    }

    /**
     * Verify doCheckDate with valid date returns error.
     */
    @Test
    public void testDoCheckDateInvalid() {
        ExcludedDate.DescriptorImpl descriptor = 
                new ExcludedDate.DescriptorImpl();
        assertEquals(FormValidation.Kind.ERROR, 
                descriptor.doCheckDate("01-01-2112z").kind);
    }

    /**
     * Verify shouldExclude ignores the year if repeatYearly is set.
     */
    @Test
    public void testShouldExcludeIgnoreYear() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "01-01-2017", true);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2019"));
        assert(instance.shouldExclude(date));
    }

    /**
     * Verify shouldExclude takes the year into account if repeatYearly not set.
     */
    @Test
    public void testShouldExcludeUseYear() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "01-01-2017", false);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2019"));
        assertFalse(instance.shouldExclude(date));
    }

    /**
     * Verify shouldExclude takes the month into account
     */
    @Test
    public void testShouldExcludeUseMonth() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "02-01-2017", false);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2017"));
        assertFalse(instance.shouldExclude(date));
    }

    /**
     * Verify shouldExclude takes the month into account even if repeatYearly is set
     */
    @Test
    public void testShouldExcludeWithRepeatYearlyUseMonth() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "02-01-2017", true);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2017"));
        assertFalse(instance.shouldExclude(date));
    }

    /**
     * Verify shouldExclude takes the day into account
     */
    @Test
    public void testShouldExcludeUseDay() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "01-02-2017", false);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2017"));
        assertFalse(instance.shouldExclude(date));
    }

    /**
     * Verify shouldExclude takes the day into account even if repeatYearly is set
     */
    @Test
    public void testShouldExcludeWithRepeatYearlyUseDay() throws ParseException {
        ExcludedDate instance = new ExcludedDate("testName", "01-02-2017", true);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        date.setTime(sdf.parse("Jan 01 2017"));
        assertFalse(instance.shouldExclude(date));
    }
}
