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

import hudson.model.Action;
import java.util.Collection;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workinghours.EnforceScheduleJobProperty;
import org.jenkinsci.plugins.workinghours.actions.EnforceBuildScheduleAction;
import org.jenkinsci.plugins.workinghours.actions.ReleaseJobAction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author jeffpearce
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkflowJob.class, WorkflowRun.class})
@PowerMockIgnore({"javax.crypto.*"})
public class ReleaseJobActionTest {

    private WorkflowJob mockWorkflowJob;
    private WorkflowRun mockWorkflowRun;
    private final long MOCK_QUEUE_ID = 2112;

    public ReleaseJobActionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        mockWorkflowJob = PowerMockito.mock(WorkflowJob.class);
        mockWorkflowRun = PowerMockito.mock(WorkflowRun.class);
        when(mockWorkflowRun.getParent()).thenReturn(mockWorkflowJob);
    }

    @After
    public void tearDown() {
    }

    /**
     * Verifies getIconFileName returns the expected result
     */
    @Test
    public void testGetIconFileName() {
        ReleaseJobAction instance = new ReleaseJobAction(mockWorkflowRun);
        assertEquals(null, instance.getIconFileName());
    }

    /**
     * Verifies getDisplayName returns the expected result
     */
    @Test
    public void testGetDisplayName() {
        ReleaseJobAction instance = new ReleaseJobAction(mockWorkflowRun);
        assertEquals(null, instance.getDisplayName());
    }

    /**
     * Verifies getUrlName returns the expected result
     */
    @Test
    public void testGetUrlName() {
        ReleaseJobAction instance = new ReleaseJobAction(mockWorkflowRun);
        assertEquals("workinghours", instance.getUrlName());
    }

    /**
     * Verifies doRelease can be called safely when there's no
     * EnforceBuildScheduleAction set on the job
     */
    @Test
    public void testDoReleaseNull() {
        ReleaseJobAction instance = new ReleaseJobAction(mockWorkflowRun);
        when(mockWorkflowJob.getAction(any())).thenReturn(null);
        instance.doRelease();
    }

    /**
     * Verifies doRelease releases the job when EnforceBuildScheduleAction us
     * set on the job
     */
    @Test
    public void testDoReleaseNotNull() {
        ReleaseJobAction instance = new ReleaseJobAction(mockWorkflowRun);
        EnforceBuildScheduleAction action = PowerMockito.mock(EnforceBuildScheduleAction.class);
        when(mockWorkflowJob.getAction(any())).thenReturn(action);
        when(mockWorkflowRun.getQueueId()).thenReturn(MOCK_QUEUE_ID);
        instance.doRelease();
        Mockito.verify(action).releaseJob();
    }

    /**
     * Verifies ReleaseJobAction.Factory type() returns the expected value
     */
    @Test
    public void testFactoryType() {
        ReleaseJobAction.Factory instance = new ReleaseJobAction.Factory();
        assertEquals(WorkflowRun.class, instance.type());
    }

    /**
     * Verifies ReleaseJobAction.Factory actionType() returns the expected value
     */
    @Test
    public void testFactoryActionType() {
        ReleaseJobAction.Factory instance = new ReleaseJobAction.Factory();
        assertEquals(ReleaseJobAction.class, instance.actionType());
    }

    /**
     * Verifies ReleaseJobAction.Factory createFor() returns an action when a
     * job is buliding
     */
    @Test
    public void testFactoryCreateForBuiding() {
        ReleaseJobAction.Factory instance = new ReleaseJobAction.Factory();

        EnforceBuildScheduleAction action = PowerMockito.mock(EnforceBuildScheduleAction.class);
        EnforceScheduleJobProperty property = PowerMockito.mock(EnforceScheduleJobProperty.class);

        when(mockWorkflowJob.getAction(any())).thenReturn(action);
        when(mockWorkflowJob.getProperty(EnforceScheduleJobProperty.class)).thenReturn(property);

        when(mockWorkflowRun.isBuilding()).thenReturn(true);
        
        Collection<? extends Action> actions = instance.createFor(mockWorkflowRun);

        assertEquals(1, actions.size());
    }

    /**
     * Verifies ReleaseJobAction.Factory createFor() returns no action when a
     * job is not building
     */
    @Test
    public void testFactoryCreateNotBuilding() {
        ReleaseJobAction.Factory instance = new ReleaseJobAction.Factory();

        EnforceBuildScheduleAction action = PowerMockito.mock(EnforceBuildScheduleAction.class);
        EnforceScheduleJobProperty property = PowerMockito.mock(EnforceScheduleJobProperty.class);

        when(mockWorkflowJob.getAction(any())).thenReturn(action);
        when(mockWorkflowJob.getProperty(EnforceScheduleJobProperty.class)).thenReturn(property);

        when(mockWorkflowRun.isBuilding()).thenReturn(false);
        
        Collection<? extends Action> actions = instance.createFor(mockWorkflowRun);

        assertEquals(0, actions.size());
    }

    /**
     * Verifies ReleaseJobAction.Factory createFor() returns no action when a
     * job does not have the EnforceScheduleJobProperty property
     */
    @Test
    public void testFactoryCreateNotEnforcing() {
        ReleaseJobAction.Factory instance = new ReleaseJobAction.Factory();

        EnforceBuildScheduleAction action = PowerMockito.mock(EnforceBuildScheduleAction.class);

        when(mockWorkflowJob.getAction(any())).thenReturn(action);
        when(mockWorkflowJob.getProperty(EnforceScheduleJobProperty.class)).thenReturn(null);

        when(mockWorkflowRun.isBuilding()).thenReturn(true);
        
        Collection<? extends Action> actions = instance.createFor(mockWorkflowRun);

        assertEquals(0, actions.size());
    }
}
