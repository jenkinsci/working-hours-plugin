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
package org.jenkinsci.plugins.workinghours.actions;

import hudson.model.InvisibleAction;
import hudson.model.Queue;
import hudson.model.Queue.Task;
import hudson.model.Run;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;

/**
 * Action attached to a project or build to manage enforcing the build schedule.
 */
public class EnforceBuildScheduleAction extends InvisibleAction {

    protected transient boolean enforcingBuildSchedule;
    private transient HashSet<Long> releasedJobs;

    /**
     * Constructor
     */
    public EnforceBuildScheduleAction() {
        this.enforcingBuildSchedule = true;
    }

    /**
     * Releases a job so it will run the next time  
     * {@link org.jenkinsci.plugins.workinghours.WorkingHoursQueueTaskDispatcher#canRun(Queue.Item item)} 
     * is called.
     * @param queueId item in the queue to release.
     */
    public void releaseJob(long queueId) {
        if (releasedJobs == null) {
            releasedJobs = new HashSet();
        }
        releasedJobs.add(queueId);
    }

    /**
     * Determine whether an item is in our list of released jobs.
     * @param item item to check.
     * @return true if release; false otherwise.
     */
    public boolean isReleased(Queue.Item item) {
        if (releasedJobs == null) {
            return false;
        }

        Task task = item.task;
        if (releasedJobs.contains(item.getId())) {
            log(Level.INFO, "Releasing item(1) %d", item.getId());
            return true;
        } else if (task instanceof ExecutorStepExecution.PlaceholderTask) {
            Run run = ((ExecutorStepExecution.PlaceholderTask) task).run();
            if (run != null
                    && releasedJobs.contains(run.getQueueId())) {
                log(Level.INFO, "Releasing item(2) %d", run.getQueueId());
                return true;
            }
        }
        return false;
    }

    /**
     * Set the release jobs.
     * @param releasedJobs set of released jobs.
     */
    public void setReleasedJobs(HashSet<Long> releasedJobs) {
        this.releasedJobs = releasedJobs;
    }

    private static void log(Level level, String format, Object... args) {
        getLogger().log(level, String.format(format, args));
    }

    private static Logger getLogger() {
        return Logger.getLogger(EnforceBuildScheduleAction.class.getName());
    }
}
