package org.jenkinsci.plugins.workinghours;

import hudson.ExtensionList;
import hudson.util.HttpResponses;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.json.JsonHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorkingHoursUI {
    private WorkingHoursConfig config;

    public WorkingHoursUI() {
    }

    public HttpResponse doDynamic(StaplerRequest request){
        if (config == null) {
            config = ExtensionList.lookup(WorkingHoursConfig.class).get(0);
        }

        String restOfPath = request.getRestOfPath();

        switch (restOfPath){
            case "list-excluded-dates":
                return getExcludedDate();
//            case "excluded-dates":
//                return addExcludedDate();
//            case "list-time-ranges":
//                return getExcludedPath();
//            case "time-ranges":
//                return getExcludedPath();

        }


        JSONObject body;
        try {
            body = JSONObject.fromObject(IOUtils.toString(request.getReader()));
        } catch (IOException e) {
            throw  new JsonHttpResponse(new JSONObject());
        }

        throw  new JsonHttpResponse(new JSONObject());
    }

    private HttpResponse getExcludedDate() {
        Map<String,String > res = new HashMap<>();
        JSONArray excludedDates = new JSONArray();
        config.getExcludedDates().forEach(item->{
            excludedDates.add(item.getJsonData());
        });
        return HttpResponses.okJSON(excludedDates);
    }

//    private HttpResponse addExcludedDate(){
//        return
//    }

}
