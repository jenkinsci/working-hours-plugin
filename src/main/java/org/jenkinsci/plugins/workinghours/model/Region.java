package org.jenkinsci.plugins.workinghours.model;

public class Region {
    private final String code;
    private final String name;

    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
