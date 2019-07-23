package org.jenkinsci.plugins.workinghours.model;

public enum RepeatPeriod {
    REPEAT_BY_WEEK(1),
    REPEAT_BY_MONTH(2),
    REPEAT_BY_YEAR(3);

    private final int value;

    RepeatPeriod(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static RepeatPeriod valueOf(int value) {
        switch (value) {
            case 1:
                return RepeatPeriod.REPEAT_BY_WEEK;
            case 2:
                return RepeatPeriod.REPEAT_BY_MONTH;
            case 3:
                return REPEAT_BY_YEAR;
            default:
                return null;
        }
    }
}

