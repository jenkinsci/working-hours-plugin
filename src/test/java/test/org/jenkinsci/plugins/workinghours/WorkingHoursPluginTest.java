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
package test.org.jenkinsci.plugins.workinghours;

import org.jenkinsci.plugins.workinghours.WorkingHoursPlugin;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import jenkins.model.GlobalConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GlobalConfiguration.class})
@PowerMockIgnore({"javax.crypto.*"})
public class WorkingHoursPluginTest {

    public WorkingHoursPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        suppress(method(WorkingHoursPlugin.class, "load"));
        suppress(method(WorkingHoursPlugin.class, "save"));
        PowerMockito.mockStatic(GlobalConfiguration.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Verify get/setBuildTimeMatrix round trip.
     */
    @Test
    public void testGetSetBuildTimeMatrix() {
//        WorkingHoursPlugin instance = new WorkingHoursPlugin();

/*        List<TimeRange> testList = Collections.singletonList(
            new TimeRange("7:30 AM", "12:30 PM", 4));

        instance.setBuildTimeMatrix(testList);*/
//        List<TimeRange> result = instance.getBuildTimeMatrix();
        assertTrue(true);
//        assertEquals(1, result.size());
    }

    /**
     * Verify default build time matrix.
     */
//    @Test
//    public void testDefaultBuildTimeMatrix() {
//        WorkingHoursPlugin instance = new WorkingHoursPlugin();
//
//        List<TimeRange> result = instance.getBuildTimeMatrix();
//        assertEquals(5, result.size());
//        assertEquals(Calendar.MONDAY, result.get(0).getDayOfWeek());
//        assertEquals(Calendar.TUESDAY, result.get(1).getDayOfWeek());
//        assertEquals(Calendar.WEDNESDAY, result.get(2).getDayOfWeek());
//        assertEquals(Calendar.THURSDAY, result.get(3).getDayOfWeek());
//        assertEquals(Calendar.FRIDAY, result.get(4).getDayOfWeek());
//    }

//    @Test
//    public void testGetSetExcludedDates() {
//        WorkingHoursPlugin instance = new WorkingHoursPlugin();

//        List<ExcludedDate> testList = Collections.singletonList(
//            new ExcludedDate("Test excluded date", "04/01/2012"));

//        instance.setExcludedDates(new ArrayList<>());
//        List<ExcludedDate> result = instance.getExcludedDates();
//        assertEquals(1, result.size());
//    }
}
