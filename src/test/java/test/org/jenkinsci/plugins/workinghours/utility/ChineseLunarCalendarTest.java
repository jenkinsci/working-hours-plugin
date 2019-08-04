package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.utils.ChineseLunarCalendar;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class ChineseLunarCalendarTest {

    @Test
    public void testLunarToSolar() {
        ChineseLunarCalendar lunar = new ChineseLunarCalendar();
        System.out.println(ChineseLunarCalendar.lunar2Solar(lunar.getLyear(), lunar.getLmonth(), lunar.getLdate(), lunar.isLeapMonth()));
        assertTrue(true);
    }
}
