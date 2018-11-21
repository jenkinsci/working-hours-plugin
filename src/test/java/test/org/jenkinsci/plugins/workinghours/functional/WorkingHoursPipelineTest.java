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
package test.org.jenkinsci.plugins.workinghours.functional;

import hudson.model.Result;
import hudson.model.queue.QueueTaskFuture;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workinghours.EnforceScheduleJobProperty;
import org.jenkinsci.plugins.workinghours.WorkingHoursConfig;
import test.org.jenkinsci.plugins.workinghours.utility.TimeRangeUtility;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author jxpearce
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkflowJob.class, WorkflowRun.class})
@PowerMockIgnore({"javax.crypto.*"})
public class WorkingHoursPipelineTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    public WorkingHoursPipelineTest() {
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
     * Verifies a job running outside normal working hours blocks
     *
     * @throws Exception
     */
    @Test
    public void testScriptedPipelineBlockOnTime() throws Exception {

        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-pipeline");
        String pipelineScript
                = "node {\n"
                + "    echo 'I ran'\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        job.addProperty(new EnforceScheduleJobProperty());

        QueueTaskFuture<WorkflowRun> run = job.scheduleBuild2(0);

        assertTrue(jenkins.jenkins.getQueue().getLeftItems().isEmpty());
        assertNotEquals(0, jenkins.jenkins.getQueue().getItems().length);

        run.cancel(true);
    }

    /**
     * Verifies a job running during normal working hours will be blocked
     * if the hours aren't configured for today
     *
     * @throws Exception
     */
    @Test
    public void testScriptedPipelineBlockOnDay() throws Exception {

        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getDifferentDayRange());
        config.save();

        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-pipeline");
        String pipelineScript
                = "node {\n"
                + "    echo 'I ran so far away'\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        job.addProperty(new EnforceScheduleJobProperty());

        QueueTaskFuture<WorkflowRun> run = job.scheduleBuild2(0);

        assertTrue(jenkins.jenkins.getQueue().getLeftItems().isEmpty());
        assertNotEquals(0, jenkins.jenkins.getQueue().getItems().length);
        
        run.cancel(true);
    }

    /**
     * Verifies a job running outside normal working hours doesn't blocks if it
     * doesn't have the job property
     *
     * @throws Exception
     */
    @Test
    public void testScriptedPipelineNoBlockOnTime() throws Exception {

        WorkingHoursConfig config = WorkingHoursConfig.get();
        config.setBuildTimeMatrix(TimeRangeUtility.getExclusiveRange());
        config.save();

        WorkflowJob job = jenkins.createProject(WorkflowJob.class, "test-pipeline");
        String pipelineScript
                = "node {\n"
                + "    echo 'I ran both night and day'\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));

        jenkins.assertBuildStatus(Result.SUCCESS, job.scheduleBuild2(0));

        assertFalse(jenkins.jenkins.getQueue().getLeftItems().isEmpty());
    }
}
