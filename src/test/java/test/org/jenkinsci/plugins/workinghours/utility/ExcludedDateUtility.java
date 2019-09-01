package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.model.DateType;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.RepeatPeriod;
import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;

import java.time.LocalDate;

public class ExcludedDateUtility {
    public static ExcludedDate getTodayInStaticExcludedDate() {
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withName("testExcludeOnStaticDate")
            .withType(DateType.TYPE_CUSTOM)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByYear(LocalDate targetDate) {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicMonth(dynamicDate.getDynamicMonth());
        dynamicDate.setDynamicWeek(DynamicDateUtil.calculateNthDayOfThisMonth(targetDate));
        dynamicDate.setDynamicWeekday(targetDate.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by year")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_MONTH)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByMonth(LocalDate targetDate) {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicWeek(DynamicDateUtil.calculateNthDayOfThisMonth(targetDate));
        dynamicDate.setDynamicWeekday(targetDate.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by month")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_MONTH)
            .build();
    }

    public static ExcludedDate getTodayInDynamicExcludedDateByWeek(LocalDate targetDate) {
        ExcludedDate.Date dynamicDate = new ExcludedDate.Date();
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        dynamicDate.setDynamic(true);
        dynamicDate.setDynamicWeekday(targetDate.getDayOfWeek().getValue());
        return ExcludedDate.Builder.anExcludedDate()
            .withStartDate(dynamicDate)
            .withName("today in dynamic date by week")
            .withType(DateType.TYPE_CUSTOM)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_WEEK)
            .build();
    }

    public static LocalDate getOccurrenceOfTargetInXYears(LocalDate targetDate, int years){
        return DynamicDateUtil.nextOccurrenceByYear(
            targetDate.getMonth().getValue(),
            DynamicDateUtil.calculateNthDayOfThisMonth(targetDate),
            targetDate.getDayOfWeek().getValue(),
            targetDate.withDayOfYear(1).plusYears(years));
    }

}
