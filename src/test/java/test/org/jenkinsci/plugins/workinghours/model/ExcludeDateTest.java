package test.org.jenkinsci.plugins.workinghours.model;

import org.jenkinsci.plugins.workinghours.model.DateType;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.model.RepeatPeriod;
import org.jenkinsci.plugins.workinghours.utils.DateTimeUtility;
import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;
import org.jenkinsci.plugins.workinghours.utils.JollydayUtil;
import org.junit.Test;
import test.org.jenkinsci.plugins.workinghours.utility.ExcludedDateUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExcludeDateTest {
    @Test
    public void testExcludeOnStaticDate() {
        LocalDate now = LocalDate.now();
        ExcludedDate excludedDate = ExcludedDateUtility.getTodayInStaticExcludedDate();
        assertTrue("today should be excluded", excludedDate.shouldExclude(now));

        assertFalse("today should be excluded", excludedDate.shouldExclude(now.plusDays(1)));
        assertFalse("today should be excluded", excludedDate.shouldExclude(now.minusDays(1)));
    }


    @Test
    public void testExcludeOnDynamicDate() {
        LocalDate testDate = LocalDate.now();
        //Larger than 4th occurrence would cause util errors when setting date larger than 31.
        if (DynamicDateUtil.calculateNthDayOfThisMonth(testDate) > 4) {
            testDate = testDate.minusWeeks(1);
        }
        LocalDate tomorrow = testDate.plusDays(1);
        LocalDate yesterday = testDate.minusDays(1);
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear(testDate).shouldExclude(testDate));
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth(testDate).shouldExclude(testDate));
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek(testDate).shouldExclude(testDate));

        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear(testDate).shouldExclude(tomorrow));
        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth(testDate).shouldExclude(tomorrow));
        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek(testDate).shouldExclude(tomorrow));

        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear(testDate).shouldExclude(yesterday));
        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth(testDate).shouldExclude(yesterday));
        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek(testDate).shouldExclude(yesterday));
    }

    @Test
    public void testExcludeOnUSThanksgivingDay() {
        org.joda.time.LocalDate thanksgivingDayInJodaTime = JollydayUtil.getHolidayThisYear("US", "THANKSGIVING").getDate();
        LocalDate thanksgivingDayThisYear = DateTimeUtility.jodaDateToLocalDate(thanksgivingDayInJodaTime);
        ExcludedDate excludedDate = ExcludedDate.Builder.anExcludedDate()
            .withType(DateType.TYPE_HOLIDAY)
            .withName("test holiday")
            .withHolidayId("THANKSGIVING")
            .withHolidayRegion("US")
            .build();

        assertTrue(excludedDate.shouldExclude(thanksgivingDayThisYear));
        assertFalse(excludedDate.shouldExclude(thanksgivingDayThisYear.minusDays(1)));
        assertFalse(excludedDate.shouldExclude(thanksgivingDayThisYear.plusDays(1)));
    }

    @Test
    public void testRepeatStaticDayWithWeekPeriod() {
        LocalDate testDate = LocalDate.now().plusWeeks(1);

        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_WEEK)
            .build();
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusWeeks(1);
        testExcludedDate.setRepeatInterval(2);
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testDate = LocalDate.now().plusWeeks(2);
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusWeeks(3);
        testExcludedDate.setRepeatInterval(2);
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatInterval(3);
        assertTrue(testExcludedDate.shouldExclude(testDate));
    }

    @Test
    public void testRepeatStaticDayWithMonthPeriod() {
        LocalDate testDate = LocalDate.now().plusMonths(1);

        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_MONTH)
            .build();
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusMonths(1);
        testExcludedDate.setRepeatInterval(2);
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testDate = LocalDate.now().plusMonths(2);
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusMonths(3);
        testExcludedDate.setRepeatInterval(2);
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatInterval(3);
        assertTrue(testExcludedDate.shouldExclude(testDate));
    }

    @Test
    public void testRepeatStaticDayWithYearPeriod() {
        LocalDate testDate = LocalDate.now().plusYears(1);

        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withRepeatInterval(1)
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_YEAR)
            .build();
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusYears(1);//One year later
        testExcludedDate.setRepeatInterval(2);//set repeat interval 2
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testDate = LocalDate.now().plusYears(2);//set to 2 years later
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = LocalDate.now().plusYears(3);//3 years later
        testExcludedDate.setRepeatInterval(2);//won't be hit when interval is 2
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatInterval(3);//will be hit when interval is 3
        assertTrue(testExcludedDate.shouldExclude(testDate));
    }

    @Test
    public void testRepeatCountWithStaticDate() {
        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withRepeatInterval(1)
            .withRepeatCount(5)
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_YEAR)
            .build();

        LocalDate testDate = LocalDate.now().plusYears(5 * 1);
        assertTrue(testExcludedDate.shouldExclude(testDate));
        testDate = LocalDate.now().plusYears(5 * 1 + 1);
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatCount(ExcludedDate.REPEAT_NO_END);
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testExcludedDate.setRepeatInterval(2);
        testExcludedDate.setRepeatCount(10);

        testDate = LocalDate.now().plusYears(2 * 10);//
        assertTrue(testExcludedDate.shouldExclude(testDate));
        testDate = LocalDate.now().plusYears(2 * (10 + 1));//One year later
        assertFalse(testExcludedDate.shouldExclude(testDate));

        testExcludedDate.setRepeatCount(11);
        assertTrue(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatCount(ExcludedDate.REPEAT_NO_END);
        assertTrue(testExcludedDate.shouldExclude(testDate));
    }

    @Test
    public void testRepeatCountWithDynamicDate() {
        LocalDate targetDate = LocalDate.now();
        if (DynamicDateUtil.calculateNthDayOfThisMonth(targetDate) > 4) {
            targetDate = targetDate.minusWeeks(1);
        }
        ExcludedDate.Date date = new ExcludedDate.Date();
        date.setDynamic(true);
        date.setDynamicMonth(targetDate.getMonth().getValue());
        date.setDynamicWeek(DynamicDateUtil.calculateNthDayOfThisMonth(targetDate));
        date.setDynamicWeekday(targetDate.getDayOfWeek().getValue());
        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withRepeatInterval(1)
            .withRepeatCount(5)
            .withStartDate(date)
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_YEAR)
            .build();
        testExcludedDate.initializeFirstOccurrence(targetDate);

        LocalDate testDate = ExcludedDateUtility.getOccurrenceOfTargetInXYears(targetDate, 5 * 1);

        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = ExcludedDateUtility.getOccurrenceOfTargetInXYears(targetDate, 5 * 1 + 1);

        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatCount(ExcludedDate.REPEAT_NO_END);

        assertTrue(testExcludedDate.shouldExclude(testDate));

        testExcludedDate.setRepeatInterval(2);
        testExcludedDate.setRepeatCount(10);

        testDate = ExcludedDateUtility.getOccurrenceOfTargetInXYears(targetDate, 2 * 10);
        assertTrue(testExcludedDate.shouldExclude(testDate));

        testDate = ExcludedDateUtility.getOccurrenceOfTargetInXYears(targetDate, 2 * (10 + 1));
        assertFalse(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatCount(11);
        assertTrue(testExcludedDate.shouldExclude(testDate));
        testExcludedDate.setRepeatCount(ExcludedDate.REPEAT_NO_END);
        assertTrue(testExcludedDate.shouldExclude(testDate));
    }

    @Test
    public void testBeyondEndDate() {
        ExcludedDate testExcludedDate = ExcludedDate.Builder.anExcludedDate()
            .withRepeat(true)
            .withNoEnd(false)
            .withEndDate(ExcludedDate.Date.fromLocalDate(LocalDate.now().minusDays(1))) //Set end date to yesterday, so all days would be beyond it.
            .withStartDate(ExcludedDate.Date.fromLocalDate(LocalDate.now()))
            .withRepeatPeriod(RepeatPeriod.REPEAT_BY_YEAR)
            .build();
        testExcludedDate.setNoEnd(true);
        assertTrue(testExcludedDate.shouldExclude(LocalDate.now()));
        testExcludedDate.setNoEnd(false);
        assertFalse(testExcludedDate.shouldExclude(LocalDate.now()));
    }
}
