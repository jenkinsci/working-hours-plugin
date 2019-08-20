package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.model.DateType;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.RepeatPeriod;
import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ExcludedDateUtility {
    public static ExcludedDate getTodayInStaticExcludedDate() {
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(ExcludedDate.Date.fromLocalDateTime(LocalDateTime.now()))
            .withName("testExcludeOnStaticDate")
            .withTimezone(ZoneId.systemDefault().getId())
            .withType(DateType.TYPE_CUSTOM)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByYear() {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        LocalDate today = LocalDate.now();
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicMonth(dynamicDate.getDynamicMonth());
        dynamicDate.setDynamicWeek(DynamicDateUtil.calculateNthDayOfThisMonth(today));
        dynamicDate.setDynamicWeekday(today.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by year")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_MONTH)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByMonth() {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        LocalDate today = LocalDate.now();
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicWeek(DynamicDateUtil.calculateNthDayOfThisMonth(today));
        dynamicDate.setDynamicWeekday(today.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by month")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_MONTH)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByWeek() {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        LocalDate today = LocalDate.now();
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicWeekday(today.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by week")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_WEEK)
            .build();
    }

    /*
     * Get a excluded date whose timezone is not at the same day with the system timezone, so the system
     * time would be excluded.
     *
     * As the max timezone delta around the world is 25 hours,
     * eg.Pacific/Niue is UTC-11, Pacific/Kiritimati is UTC+14
     * So we can set one of them and there would be a date which is different from now.
     * @return A excluded date which has a different day.
     */
    public static ExcludedDate getAnotherDayExcludedDate() {
        ExcludedDate.Builder builder = ExcludedDate.Builder.anExcludedDate()
            .withStartDate(ExcludedDate.Date.fromLocalDateTime(LocalDateTime.now()))
            .withName("get timezone which is another day")
            .withType(DateType.TYPE_CUSTOM);

        LocalDate now = LocalDate.now();
        if (ZonedDateTime.now(ZoneId.of("Pacific/Niue")).toLocalDate().getDayOfYear() == now.getDayOfYear()) {
            builder.withTimezone("Pacific/Kiritimati");
        } else {
            builder.withTimezone("Pacific/Niue");
        }

        return builder.build();
    }
}
