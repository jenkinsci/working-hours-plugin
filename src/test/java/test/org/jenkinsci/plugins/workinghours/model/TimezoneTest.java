package test.org.jenkinsci.plugins.workinghours.model;


import org.jenkinsci.plugins.workinghours.model.ExcludedDate;
import org.junit.Test;
import test.org.jenkinsci.plugins.workinghours.utility.ExcludedDateUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class TimezoneTest {
    @Test
    public void testDifferentTimezone() {
        //As the max timezone delta around the world is 25 hours,
        // eg.Pacific/Niue is UTC-11, Pacific/Kiritimati is UTC+14
        //So we can set one of them and there would be a date which is different from now.
        ExcludedDate excludedDateAnotherDate = ExcludedDateUtility.getAnotherDayExcludedDate();
        assertFalse("today should not be excluded because the excluded date is yesterday/tomorrow",
            excludedDateAnotherDate.shouldExclude(null));
    }
}
