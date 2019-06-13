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
package org.jenkinsci.plugins.workinghours;

import org.jenkinsci.plugins.workinghours.actions.EnforceBuildScheduleAction;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import hudson.Extension;
import hudson.model.Actionable;
import hudson.model.Node;
import hudson.model.Queue;
import hudson.model.Queue.Task;
import hudson.model.Run;
import hudson.model.queue.CauseOfBlockage;
import hudson.model.queue.QueueTaskDispatcher;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution;

/**
 *
 * @author jxpearce
 */

/**
 * QueueTaskDispatcher implementation that can block jobs configured with the
 * enforceBuildSchedule property outside of configured working hours.
 * 
 * @author jxpearce@godaddy.com
 */
@Extension(optional = true)
public class WorkingHoursQueueTaskDispatcher extends QueueTaskDispatcher {

    /**
     * {@inheritDoc}
     * Determines whether the job has a EnforceScheduleJobProperty,
     * and if so, checks whether the current time is allowed, blocking it if not
     */
    @Override
    public CauseOfBlockage canRun(Queue.Item item) {

        // Don't block tasks which don't have owners. This filters out both
        // freestyle jobs and the task for the Job. The latter is important 
        // because we want to block the Run not the Job.
        if (item.task == item.task.getOwnerTask()) {
            return super.canRun(item);
        }
        
        // We will only consider blocking PlaceholderTasks. We can get the
        // run directly from such tasks.
        if (!(item.task instanceof ExecutorStepExecution.PlaceholderTask)) {
            return super.canRun(item);
        }
        Task ownerTask = item.task.getOwnerTask();
        if (ownerTask instanceof WorkflowJob) {
            WorkflowJob workflowJob = (WorkflowJob) ownerTask;
            Run workflowRun = ((ExecutorStepExecution.PlaceholderTask)item.task).run();
            EnforceScheduleJobProperty prop = workflowJob.getProperty(EnforceScheduleJobProperty.class);
            if (prop != null) {
                if (prop.getBranches() == null || prop.getBranches().size() == 0 || prop.getBranches().contains(workflowJob.getDisplayName())) {
                    if (!canRunNow(workflowRun, item)) {
                        log(Level.INFO, "Blocking item %d", item.getId());
                        return CauseOfBlockage.fromMessage(Messages._WorkingHoursQueueTaskDispatcher_Offline());
                    }
                }
            }
        }
        return super.canRun(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CauseOfBlockage canTake(Node node, Queue.BuildableItem item) {
        return super.canTake(node, item);
    }

    /**
     * Determines if a queue item has been manually released.
     * @param action The enforce build schedule action.
     * @param item The queue item to check.
     * @return true if the item was manually released; false otherwise.
     */
    public boolean isReleased(EnforceBuildScheduleAction action,
            Queue.Item item) {
        return action != null && action.isReleased();
    }

    /**
     * Determines whether a queue item can run at the current moment.
     * @param itemActionable The item being checked.
     * @param item The queue item to check.     
     * @return true if the item can run now; false otherwise.
     */
    public boolean canRunNow(Actionable itemActionable,
            Queue.Item item) {
        Calendar now = Calendar.getInstance();

        EnforceBuildScheduleAction action = itemActionable.getAction(EnforceBuildScheduleAction.class);

        WorkingHoursConfig config = WorkingHoursConfig.get();

        // Check whether today should be excluded according to the excluded dates we set.
        for (ExcludedDate excludedDate : config.getExcludedDates()) {
            if(excludedDate.shouldExclude(now)){
                return false;
            }
        }

        for (TimeRange allowableTime : config.getBuildTimeMatrix()) {
            if (allowableTime.includesTime(now)) {
                if (action != null) {
                    // Mark the action as released for book-keeping
                    action.markReleased();
                }
                return true;
            }
        }
        if (action == null) {
            action = new EnforceBuildScheduleAction();
            itemActionable.addAction(action);
        }

        if (isReleased(action, item)) {
            return true;
        }
        return false;
    }

    private static void log(Level level, String format, Object... args) {
        getLogger().log(level, String.format(format, args));
    }

    private static Logger getLogger() {
        return Logger.getLogger(WorkingHoursQueueTaskDispatcher.class.getName());
    }
}
