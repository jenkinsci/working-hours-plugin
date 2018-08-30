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

import hudson.Extension;
import hudson.model.Action;
import java.util.Collection;
import java.util.Collections;
import jenkins.model.TransientActionFactory;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workinghours.EnforceScheduleJobProperty;
import org.kohsuke.stapler.interceptor.RequirePOST;

/**
 * Action added to builds which may need to be released.
 */
public class ReleaseJobAction implements Action {

    public final WorkflowRun target;

    /**
     * Constructor
     *
     * @param job the build the action is for.
     */
    public ReleaseJobAction(WorkflowRun job) {
        this.target = job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIconFileName() {
        return null; // special presentation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return null; // special presentation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlName() {
        return "workinghours";
    }

    /**
     * Releases the build referenced by the action to run now.
     */
    @RequirePOST
    public void doRelease() {
        EnforceBuildScheduleAction action = target.getParent().getAction(EnforceBuildScheduleAction.class);
        if (action != null) {
            action.releaseJob(target.getQueueId());
        }
    }

    /**
     * Dynamic action factory which determines whether to add the action to a
     * build.
     */
    @Extension
    public static class Factory extends TransientActionFactory<WorkflowRun> {

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<WorkflowRun> type() {
            return WorkflowRun.class;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<? extends Action> actionType() {
            return ReleaseJobAction.class;
        }

        /**
         * {@inheritDoc}
         * The action is only added to builds which have the EnforceScheduleJobProperty
         * job property.
         */
        @Override
        public Collection<? extends Action> createFor(WorkflowRun target) {
            EnforceScheduleJobProperty prop = target.getParent().getProperty(EnforceScheduleJobProperty.class);

            if (prop != null && target.isBuilding()) {
                return Collections.singleton(new ReleaseJobAction(target));
            }
            return Collections.emptySet();
        }
    }
}
