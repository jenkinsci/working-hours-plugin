package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DynamicUtilTest {
    @Test
    public void testNextOccurrenceByMonth() {
        LocalDate testDate = LocalDate.of(2019, 7, 22);
        assertTrue(LocalDate.of(2019, 8, 5).isEqual(DynamicDateUtil.nextOccurrenceByMonth(1, 1,testDate)));
        assertTrue(LocalDate.of(2019, 7, 29).isEqual(DynamicDateUtil.nextOccurrenceByMonth(5, 1,testDate)));
        assertTrue(LocalDate.of(2019, 8, 2).isEqual(DynamicDateUtil.nextOccurrenceByMonth(1, 5,testDate)));
        assertTrue(LocalDate.of(2019, 8, 21).isEqual(DynamicDateUtil.nextOccurrenceByMonth(3, 3,testDate)));
        assertTrue(LocalDate.of(2019, 7, 22).isEqual(DynamicDateUtil.nextOccurrenceByMonth(4, 1,testDate)));

        LocalDate testDate2 = LocalDate.of(2019, 8, 20);
        assertTrue(LocalDate.of(2019, 8, 20).isEqual(DynamicDateUtil.nextOccurrenceByMonth(3, 2,testDate2)));

    }

    @Test
    public void testNextOccurrenceByYear() {
        LocalDate testDate = LocalDate.of(2019, 7, 22);
        assertTrue(LocalDate.of(2020, 1, 6).isEqual(DynamicDateUtil.nextOccurrenceByYear(1, 1, 1, testDate)));
        assertTrue(LocalDate.of(2019, 11, 20).isEqual(DynamicDateUtil.nextOccurrenceByYear(11, 3, 3, testDate)));
        assertTrue(LocalDate.of(2020, 5, 8).isEqual(DynamicDateUtil.nextOccurrenceByYear(5, 2, 5, testDate)));
        assertTrue(LocalDate.of(2020, 7, 6).isEqual(DynamicDateUtil.nextOccurrenceByYear(7, 1, 1, testDate)));
        assertTrue(LocalDate.of(2019, 7, 22).isEqual(DynamicDateUtil.nextOccurrenceByYear(7, 4, 1, testDate)));
        assertTrue(LocalDate.of(2019, 7, 23).isEqual(DynamicDateUtil.nextOccurrenceByYear(7, 4, 2, testDate)));
        assertTrue(LocalDate.of(2020, 7, 19).isEqual(DynamicDateUtil.nextOccurrenceByYear(7, 3, 0, testDate)));
    }

    @Test
    public void testCalculateNthDayOfThisMonth(){
        LocalDate testDate1 = LocalDate.of(2019,8,20);
        //2019,8,20 is the 3rd occurrence of Tuesday.
        LocalDate testDate2 = LocalDate.of(2019,8,22);
        //2019,8,22 is the 4th occurrence of Thursday.
        assertEquals(3,DynamicDateUtil.calculateNthDayOfThisMonth(testDate1));
        assertEquals(4,DynamicDateUtil.calculateNthDayOfThisMonth(testDate2));
    }
}
