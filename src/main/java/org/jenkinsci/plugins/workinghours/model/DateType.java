package org.jenkinsci.plugins.workinghours.model;

public enum DateType {
    TYPE_CUSTOM(1),
    TYPE_HOLIDAY(2);

    private final int value;

    DateType(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static DateType valueOf(int value) {
        switch (value) {
            case 1:
                return DateType.TYPE_CUSTOM;
            case 2:
                return DateType.TYPE_HOLIDAY;
            default:
                return null;
        }
    }
}
