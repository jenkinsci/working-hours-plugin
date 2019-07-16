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

import java.util.Date;

/**
 * Action attached to a project or build to manage enforcing the build schedule.
 */
public class EnforceBuildScheduleAction extends InvisibleAction {

    protected transient boolean enforcingBuildSchedule;
    private long releasedTimeStamp;

    /**
     * Constructor
     */
    public EnforceBuildScheduleAction() {
        this.enforcingBuildSchedule = true;
        releasedTimeStamp = 0;
    }

    /**
     * Releases the build the action refers to. It will start running again when
     * {@link org.jenkinsci.plugins.workinghours.WorkingHoursQueueTaskDispatcher#canRun(Queue.Item item)} 
     * is called for the associated blocked queue item.
     */
    public void releaseJob() {
        this.enforcingBuildSchedule = false;
        markReleased();
    }

    /**
     * Determine whether the build this action refers to has been released.
     *      * @return true if released; false otherwise.
     */
    public boolean isReleased() {
        return !this.enforcingBuildSchedule;
    }

    /**
     * Marks a job as released, and sets the releasedTimeStamp if it isn't already
     * set.
     */
    public void markReleased() {
        if (releasedTimeStamp == 0) {
            releasedTimeStamp = new Date().getTime();
        }
    }
    
    /** 
     * Gets the released timestamp.
     * @return time job was released (in unix timestamp).
     */
    public long getReleasedTimeStamp() {
        return releasedTimeStamp;
    }
}
