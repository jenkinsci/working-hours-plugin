package test.org.jenkinsci.plugins.workinghours.utility;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class JollyDayTest {
    @Test
    public void testGetHolidays() {
        HolidayManager manager = HolidayManager.getInstance("US");
        Set result = manager.getHolidays(2019);
        result.forEach(System.out::println);
        System.out.println(HolidayManager.getSupportedCalendarCodes());
        System.out.println(Arrays.toString(HolidayCalendar.values()));
        assertTrue(true);
    }

    @Test
    public void testCalendars() {
        System.out.println(123);
        for (HolidayCalendar l : HolidayCalendar.values())
        {
            String name = l.getId();
            System.out.println(name);
            // Do stuff here
        }

        for (String key : HolidayManager.getSupportedCalendarCodes())
        {
            System.out.println(key);
            // Do stuff here
        }

        assertTrue(true);
    }
}
