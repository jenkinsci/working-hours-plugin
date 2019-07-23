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

import org.jenkinsci.plugins.workinghours.utils.DateTimeUtility;
import org.junit.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class DateTimeUtilityTest {

    public DateTimeUtilityTest() {
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
     * Verifies localTime can parse 24 hour time (AM).
     */
    @Test
    public void testLocalTimeHHMMAm() {
        LocalTime result = DateTimeUtility.localTime("0930");
        assertEquals("09:30", result.toString());
    }

    /**
     * Verifies localTime can parse 24 hour time (PM).
     */
    @Test
    public void testLocalTimeHHMMPm() {
        LocalTime result = DateTimeUtility.localTime("2300");
        assertEquals("23:00", result.toString());
    }

    /**
     * Verifies localTime can parse 24 hour time (AM).
     */
    @Test
    public void testLocalTimeHHMMAm2() {
        LocalTime result = DateTimeUtility.localTime("09:30");
        assertEquals("09:30", result.toString());
    }

    /**
     * Verifies localTime can parse 24 hour time (PM).
     */
    @Test
    public void testLocalTimeHHMMPm2() {
        LocalTime result = DateTimeUtility.localTime("23:00");
        assertEquals("23:00", result.toString());
    }

    /**
     * Verifies isValidTime returns true for valid time.
     */
    @Test
    public void testIsValidTimeFalse() {
        assertFalse(DateTimeUtility.isValidTime("10 1/2"));
    }

    /**
     * Verifies localDate can pass mm/dd/yyyy date.
     */
    @Test
    public void testLocalDateSlash() {
        LocalDate result = DateTimeUtility.localDate("11/12/1970");
        assertEquals("1970-11-12", result.toString());
    }

    /**
     * Verifies localDate can pass mm-dd-yyyy date.
     */
    @Test
    public void testLocalDateHyphen() {
        LocalDate result = DateTimeUtility.localDate("11-12-1972");
        assertEquals("1972-11-12", result.toString());
    }

    /**
     * Verifies isValidDate method returns true for a valid date.
     */
    @Test
    public void testIsValidDateTrue() {
        assertTrue(DateTimeUtility.isValidDate("11/12/1970"));
    }

    /**
     * Verifies isValidDate method returns false for an invalid date.
     */
    @Test
    public void testIsValidDateFalse() {
        assertFalse(DateTimeUtility.isValidDate("11 12 1970"));
    }

    /**
     * Verifies DateTimeUtility can not be instantiated, since it's a pure
     * utility class.
     */
    @Test
    public void testNoInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        java.lang.reflect.Constructor<?>[] ctors = 
                DateTimeUtility.class.getDeclaredConstructors();
        assertEquals("DateTimeUtility class should only have one constructor",
                1, ctors.length);
        java.lang.reflect.Constructor<?> ctor = ctors[0];
        assertFalse("DateTimeUtility class constructor should be inaccessible",
                ctor.isAccessible());
        ctor.setAccessible(true);
        ctor.newInstance();    
    }
}
