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
package test.org.jenkinsci.plugins.workinghours.actions;

import hudson.model.Queue.WaitingItem;
import hudson.model.Run;
import java.util.Calendar;
import java.util.Collections;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;
import org.jenkinsci.plugins.workinghours.actions.EnforceBuildScheduleAction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 *
 * @author jeffpearce
 */
public class EnforceBuildScheduleActionTest {
    
    public EnforceBuildScheduleActionTest() {
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
     * Verifies isReleased returns true when the item passed in has been released
     */
    @Test
    public void testIsReleasedReleasedItem() {
        EnforceBuildScheduleAction instance = new EnforceBuildScheduleAction();
        instance.releaseJob();
        
        assertEquals(true, instance.isReleased());
    }

    /**
     * Verifies isReleased returns true when the item passed in us a placeholder task
     */
    @Test
    public void testIsReleasedPlaceholderTask() {
        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        
        WorkflowRun mockRun = mock(WorkflowRun.class);
        when(task.run()).thenReturn((Run)mockRun);

        EnforceBuildScheduleAction instance = new EnforceBuildScheduleAction();
        instance.releaseJob();
        
        assertEquals(true, instance.isReleased());
    }

   /**
     * Verifies getReleased returns 0 before markReleased is called.
     */
    @Test
    public void testGetReleasedDefault() {
        EnforceBuildScheduleAction instance = new EnforceBuildScheduleAction();
        
        assertEquals(0, instance.getReleasedTimeStamp());
    }

       /**
     * Verifies getReleased returns non-0 after markReleased is called.
     */
    @Test
    public void testMarkReleased() {
        EnforceBuildScheduleAction instance = new EnforceBuildScheduleAction();
        
        instance.markReleased();          
        
        assertNotEquals(0, instance.getReleasedTimeStamp());
    }

}
