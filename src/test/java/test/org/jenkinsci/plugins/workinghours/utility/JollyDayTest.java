package test.org.jenkinsci.plugins.workinghours.utility;

import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import net.sf.json.JSONArray;
import org.jenkinsci.plugins.workinghours.utils.HolidayUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
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
        for (HolidayCalendar l : HolidayCalendar.values()) {
            String name = l.getId();
            System.out.println(name);
            // Do stuff here
        }

        for (String key : HolidayManager.getSupportedCalendarCodes()) {
            System.out.println(key);
            // Do stuff here
        }

        assertTrue(true);
    }

    @Test
    public void testGetCertainHoliday() {
        System.out.println(HolidayManager.getInstance("AR").getHolidays(Calendar.getInstance().get(Calendar.YEAR)));
        HolidayManager.getInstance("AR").getHolidays(Calendar.getInstance().get(Calendar.YEAR)).stream().filter(item -> item.getPropertiesKey().equals("MALVINAS")).forEach(System.out::println);
        System.out.println(HolidayManager.getInstance("AR").getHolidays(Calendar.getInstance().get(Calendar.YEAR) + 1));
        assertTrue(true);
    }

    @Test
    public void testGetTwoYearsHoliday() {
        System.out.println(JSONArray.fromObject(HolidayUtil.getTwoYearsHoliday("US")));
        assertTrue(true);
    }
}
