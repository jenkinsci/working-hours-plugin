package org.jenkinsci.plugins.workinghours;

import hudson.ExtensionList;
import hudson.util.HttpResponses;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.TimeRange;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.json.JsonHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkingHoursUI {
    private WorkingHoursPlugin config;

    public WorkingHoursUI() {
    }

    /**
     * Handle dynamic routes, and do process on set/get excluded
     * dates and time ranges.
     *
     * @param request Request object passed by Stapler
     * @return A JSON Response that handle back to client.
     */
    public HttpResponse doDynamic(StaplerRequest request) {
        if (config == null) {
            config = ExtensionList.lookup(WorkingHoursPlugin.class).get(0);
        }

        String restOfPath = request.getRestOfPath();

        String[] pathTokens = restOfPath.split("/");
        List<String> params = new ArrayList<>();

        for (String pathToken : pathTokens) {
            if (pathToken.length() > 0) {
                params.add(pathToken);
            }
        }

        // TODO: 28/5/2019 use request method to classify manipulations

        switch (params.get(0)) {
            case "list-excluded-dates":
                return listExcludedDate(request);
            case "set-excluded-dates":
                return setExcludedDates(request);
            case "list-time-ranges":
                return listTimeRanges(request);
            case "set-time-ranges":
                return setTimeRanges(request);
        }

        // TODO: 30/5/2019 Implement or find a detailed error response Object
        throw new JsonHttpResponse(new JSONObject());

    }

    private HttpResponse listTimeRanges(StaplerRequest request) {
        return HttpResponses.okJSON(serializeTimeRanges());

    }

    private HttpResponse setTimeRanges(StaplerRequest request) {
        List<TimeRange> newTimeRanges = new ArrayList();

        JSONArray timeRangesJson = (JSONArray) getRequestBody(request).get("data");
        for (Object o : timeRangesJson) {
            newTimeRanges.add(new TimeRange((String) o));
        }

        config.setBuildTimeMatrix(newTimeRanges);

        return HttpResponses.okJSON(serializeTimeRanges());
    }

    private HttpResponse setExcludedDates(StaplerRequest request) {
        List<ExcludedDate> newExcludedDates = new ArrayList();

        JSONArray excludedDatesJson = (JSONArray) getRequestBody(request).get("data");
        for (Object o : excludedDatesJson) {
            newExcludedDates.add(new ExcludedDate((String) o));
        }

        config.setExcludedDates(newExcludedDates);

        return HttpResponses.okJSON(serializeExcludedDates());
    }

    /**
     * Serialize model excluded dates to JSONObejct
     *
     * @return An array of serialized excluded dates.
     */
    private JSONArray serializeExcludedDates() {
//        Map<String, String> res = new HashMap<>();
        JSONArray excludedDates = new JSONArray();
        config.getExcludedDates().forEach(item -> {
            excludedDates.add(item.getJsonData());
        });
        return excludedDates;
    }

    /**
     * Serialize model time ranges to JSONObejct
     *
     * @return JSONArray that contains a list of serialized time range data.
     */
    private JSONArray serializeTimeRanges() {
//        Map<String, String> res = new HashMap<>();
        JSONArray timeRanges = new JSONArray();
        config.getBuildTimeMatrix().forEach(item -> {
            timeRanges.add(item.getJsonData());
        });
        return timeRanges;
    }


    /**
     * Return all the excluded dates in JSON.
     *
     * @param request Request object passed by Stapler
     * @return Response to send back to client.
     */
    private HttpResponse listExcludedDate(StaplerRequest request) {
        return HttpResponses.okJSON(serializeExcludedDates());
    }


    /**
     * Get body from a post request.
     *
     * @param request Request object passed by Stapler
     * @return A JSONObject that contains the request body.
     */
    private JSONObject getRequestBody(StaplerRequest request) {
        JSONObject body;
        try {
            body = JSONObject.fromObject(IOUtils.toString(request.getReader()));
        } catch (IOException e) {
            // TODO: 28/5/2019 Implement a detailed Error Response
            throw new JsonHttpResponse(new JSONObject());
        }
        return body;
    }
}
