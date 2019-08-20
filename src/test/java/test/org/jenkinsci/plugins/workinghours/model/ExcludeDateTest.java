package test.org.jenkinsci.plugins.workinghours.model;

import org.jenkinsci.plugins.workinghours.model.DateType;
import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.jenkinsci.plugins.workinghours.utils.DateTimeUtility;
import org.jenkinsci.plugins.workinghours.utils.JollydayUtil;
import org.junit.Test;
import test.org.jenkinsci.plugins.workinghours.utility.ExcludedDateUtility;

import java.time.LocalDate;

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
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        LocalDate yesterday = now.minusDays(1);
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear().shouldExclude(now));
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth().shouldExclude(now));
        assertTrue("today should be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek().shouldExclude(now));

        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear().shouldExclude(tomorrow));
        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth().shouldExclude(tomorrow));
        assertFalse("tomorrow should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek().shouldExclude(tomorrow));

        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByYear().shouldExclude(yesterday));
        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByMonth().shouldExclude(yesterday));
        assertFalse("yesterday should not be excluded", ExcludedDateUtility.getTodayInDynamicExcludedDateByWeek().shouldExclude(yesterday));
    }

    @Test
    public void testExcludeOnUSThanksgivingDay(){
        org.joda.time.LocalDate thanksgivingDayInJodaTime = JollydayUtil.getHolidayThisYear("US","THANKSGIVING").getDate();
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
}
