/*
 *  Copyright (C) 2019  Jack Shen
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses.
 */
package org.jenkinsci.plugins.workinghours;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import hudson.Extension;
import hudson.model.ManagementLink;
import org.kohsuke.stapler.StaplerProxy;

import java.util.logging.Logger;

/**
 * An entry point for Working Hours Plugin's config page.
 */
@Extension
public class WorkingHoursMgmtLink extends ManagementLink implements StaplerProxy {
    private static final Logger LOGGER = Logger.getLogger("hudson.plugins.thinbackup");

    @Inject
    WorkingHoursUI app;

    @Override
    public String getDisplayName() {
        return "Working Hours Config";
    }

    @Override
    public String getIconFileName() {
        return "/plugin/working-hours/images/icon.png";
    }

    @Override
    public String getUrlName() {
        return "working-hours";
    }

    @Override
    public String getDescription() {
        return "Set allowed time ranges and excluded dates to schedule a build";
    }


    @Override
    public Object getTarget() {
        return app;
    }

    /**
     * Provides implementation of BlueOceanUI
     */
    @Extension
    public static class ModuleImpl implements Module {

        @Override
        public void configure(Binder binder) {
            binder.bind(WorkingHoursUI.class).toInstance(new WorkingHoursUI());
        }
    }

}
