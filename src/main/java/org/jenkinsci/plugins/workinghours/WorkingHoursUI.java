package org.jenkinsci.plugins.workinghours;

import hudson.ExtensionList;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;

public class WorkingHoursUI {
    private WorkingHoursConfig config;

    public WorkingHoursUI() {
        config = ExtensionList.lookup(WorkingHoursConfig.class).get(0);
    }

    public HttpResponse doDynamic(StaplerRequest request){
        return HttpResponses.ok();
    }

}
