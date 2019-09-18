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

import hudson.model.*;
import hudson.model.Queue.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;
import org.jenkinsci.plugins.workinghours.EnforceScheduleJobProperty;
import org.jenkinsci.plugins.workinghours.WorkingHoursConfig;
import org.jenkinsci.plugins.workinghours.WorkingHoursQueueTaskDispatcher;
import org.jenkinsci.plugins.workinghours.actions.EnforceBuildScheduleAction;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import test.org.jenkinsci.plugins.workinghours.utility.TimeRangeUtility;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkflowJob.class, WorkflowRun.class})
@PowerMockIgnore({"javax.crypto.*"})
public class WorkingHoursQueueTaskDispatcherTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    public WorkingHoursQueueTaskDispatcherTest() {
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
     * Verifies canRun returns null when the task has itself as a parent (for
     * example when it is called for the project as a whole).
     */
    @Test
    public void testCanRun() {

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        when(task.getOwnerTask()).thenReturn(task);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canRun(item));
    }

    /**
     * Verifies canRun won't block jobs which aren't pipeline.
     */
    @Test
    public void testCanRunNotWorkflow() {

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        when(task.getOwnerTask()).thenReturn(task);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canRun(item));
    }

    /**
     * Verifies canRun doesn't block jobs without the EnforceScheduleJobProperty
     * property.
     */
    @Test
    public void testCanRunWorkflow() {

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canRun(item));
    }

    /**
     * Verifies canRun returns a blockage reason when the job should be blocked
     * property.
     */
    @Test
    public void testCanRunBlocked() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNotNull(instance.canRun(item));
    }

    /**
     * Verifies that canRun doesn't block tasks which aren't placeholder tasks.
     */
    @Test
    public void testCanRunBlockedNotPlaceholder() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        Task task = mock(Task.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);

        when(task.getOwnerTask()).thenReturn(job);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canRun(item));
    }

    /**
     * Verifies that canRun returns a blockage reason when branches is null
     */
    @Test
    public void testCanRunBlockedNullBranches() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);
        when(prop.getBranches()).thenReturn(null);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNotNull(instance.canRun(item));
    }

    /**
     * Verifies that canRun returns a blockage reason when branches is empty.
     */
    @Test
    public void testCanRunBlockedEmptyBranches() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);
        when(prop.getBranches()).thenReturn(new ArrayList<>());

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNotNull(instance.canRun(item));
    }

    /**
     * Verifies that canRun returns a blockage reason when not a MultibranchPipeline
     */
    @Test
    public void testCanRunBlockedNonMultibranchPipeline() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);
        when(job.getParent()).thenReturn(mock(Hudson.class));
        when(prop.getBranches()).thenReturn(new ArrayList<>(Arrays.asList("TestBranch")));

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNotNull(instance.canRun(item));
    }


    /*
     * Verifies that canRun returns a blockage reason when current branch doesn't match branches provided
     *
    @Test
    public void testCanRunBlockedNonMatchingBranches() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);
        doReturn(mock(WorkflowMultiBranchProject.class)).when(job).getParent();
        when(job.getDisplayName()).thenReturn("master");
        when(prop.getBranches()).thenReturn(new ArrayList<>(Arrays.asList("TestBranch")));

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canRun(item));
    }

    /**
     * Verifies that canRun blocks tasks when current branch matches a branch provided and is a MultibranchPipeline
     *
    @Test
    public void testCanRunBlockedMatchingBranches() throws WrongTypeOfReturnValue {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem item = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);

        EnforceScheduleJobProperty prop = mock(EnforceScheduleJobProperty.class);
        WorkflowJob job = mock(WorkflowJob.class);
        Run run = mock(Run.class);

        when(task.getOwnerTask()).thenReturn(job);
        when(task.run()).thenReturn(run);
        when(job.getProperty(EnforceScheduleJobProperty.class)).thenReturn(prop);
        when(job.getDisplayName()).thenReturn("master");
        doReturn(mock(WorkflowMultiBranchProject.class)).when(job).getParent();
        when(prop.getBranches()).thenReturn(new ArrayList<>(Arrays.asList("master")));

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNotNull(instance.canRun(item));
    }
    /

    /**
     * Verifies canTake won't block jobs
     */
    @Test
    public void testCanTakeReturnsNull() {

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        Node mockNode = mock(Node.class);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertNull(instance.canTake(mockNode, item));
    }

    /**
     * Verifies isReleased returns false if the action is null
     */
    @Test
    public void testIsReleasedNullAction() {
        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        assertFalse(instance.isReleased(null, item));
    }

    /**
     * Verifies isReleased returns false if the action returns false
     */
    @Test
    public void testIsReleasedFalseAction() {
        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();

        EnforceBuildScheduleAction action = mock(EnforceBuildScheduleAction.class);
        when(action.isReleased()).thenReturn(false);
        assertFalse(instance.isReleased(action, item));
    }

    /**
     * Verifies isReleased returns true if the action returns true
     */
    @Test
    public void testIsReleasedTrueAction() {
        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();

        EnforceBuildScheduleAction action = mock(EnforceBuildScheduleAction.class);
        when(action.isReleased()).thenReturn(true);
        assertTrue(instance.isReleased(action, item));
    }

    /**
     * Verifies canRunNow returns true if current time is allowable
     */
    @Test
    public void testCanRunNowInclusiveRange() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getInclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        Actionable project = mock(Actionable.class);
        EnforceBuildScheduleAction action = mock(EnforceBuildScheduleAction.class);
        when(project.getAction(any())).thenReturn(action);

        assertTrue(instance.canRunNow(project, item));
        verify(action).markReleased();
    }

    /**
     * Verifies canRunNow returns false if current time is not allowed
     */
    @Test
    public void testCanRunNowExclusiveRange() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        Actionable project = mock(Actionable.class);

        assertFalse(instance.canRunNow(project, item));
    }

    /**
     * Verifies canRunNow returns false if today is excluded.
     */
    @Test
    public void testCanRunNowExclusiveDate() {
        //Configure with excluded date.
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setExcludedDates(TimeRangeUtility.getExclusiveDate());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        Actionable project = mock(Actionable.class);

        assertFalse(instance.canRunNow(project, item));
    }

    /**
     * Verifies canRunNow returns true if today is not excluded.
     */
    @Test
    public void testCanRunNowInclusiveDate() {
        //Configure with excluded date.
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setExcludedDates(TimeRangeUtility.getInclusiveDate());
        //Also set inclusive time range to make it able to run.
        config.setBuildTimeMatrix(TimeRangeUtility.getInclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();
        Actionable project = mock(Actionable.class);

        assertTrue(instance.canRunNow(project, item));
    }

    /**
     * Verifies canRunNow returns true if current time is not allowed, but
     * the job has been released
     */
    @Test
    public void testCanRunNowReleased() {
        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        ExecutorStepExecution.PlaceholderTask task = mock(ExecutorStepExecution.PlaceholderTask.class);
        Queue.WaitingItem waitingItem = new Queue.WaitingItem(Calendar.getInstance(), task, Collections.EMPTY_LIST);
        Queue.BuildableItem item = new Queue.BuildableItem(waitingItem);

        WorkingHoursQueueTaskDispatcher instance = new WorkingHoursQueueTaskDispatcher();

        EnforceBuildScheduleAction action = mock(EnforceBuildScheduleAction.class);
        when(action.isReleased()).thenReturn(true);
        Actionable project = mock(Actionable.class);
        when(project.getAction(any())).thenReturn(action);

        assertTrue(instance.canRunNow(project, item));
    }
}
