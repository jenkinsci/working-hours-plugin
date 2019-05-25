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

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.TaskListener;
import hudson.model.Hudson;
import hudson.triggers.Trigger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * A backup solution for Hudson. Backs up configuration files from Hudson and its jobs.
 *
 * Originally based on the Backup plugin by Vincent Sellier, Manufacture Fran�aise des Pneumatiques Michelin, Romain
 * Seguy, et.al. Subsequently heavily modified.
 */
@Extension
public class WorkingHoursMgmtLink extends ManagementLink {
    private static final Logger LOGGER = Logger.getLogger("hudson.plugins.thinbackup");

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


}
