/*
 *  Copyright (C) 2011  Matthias Steinkogler, Thomas Fürer
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

import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerProxy;

/**
 * A backup solution for Hudson. Backs up configuration files from Hudson and its jobs.
 * <p>
 * Originally based on the Backup plugin by Vincent Sellier, Manufacture Fran�aise des Pneumatiques Michelin, Romain
 * Seguy, et.al. Subsequently heavily modified.
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
        return "/plugin/working-hours/images/play.png";
    }

    @Override
    public String getUrlName() {
        return "working-hours";
    }

    @Override
    public String getDescription() {
        return "Working Hours Config";
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
