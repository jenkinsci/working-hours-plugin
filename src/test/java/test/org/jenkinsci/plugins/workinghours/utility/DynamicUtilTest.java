package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.utils.DynamicDateUtil;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public class DynamicUtilTest {
    @Test
    public void testNextOccurrenceByMonth() {
        assertTrue(LocalDate.of(2019, 8, 5).isEqual(DynamicDateUtil.nextOccurrenceByMonth(1, 1)));
        assertTrue(LocalDate.of(2019, 7, 29).isEqual(DynamicDateUtil.nextOccurrenceByMonth(5, 1)));
        assertTrue(LocalDate.of(2019, 8, 2).isEqual(DynamicDateUtil.nextOccurrenceByMonth(1, 5)));
        assertTrue(LocalDate.of(2019, 8, 21).isEqual(DynamicDateUtil.nextOccurrenceByMonth(3, 3)));
    }

    @Test
    public void testNextOccurrenceByYear() {
        assertTrue(LocalDate.of(2020, 1, 6).isEqual(DynamicDateUtil.nextOccurrenceByYear(1, 1,1)));
        assertTrue(LocalDate.of(2019, 11, 20).isEqual(DynamicDateUtil.nextOccurrenceByYear(11,3, 3)));
        assertTrue(LocalDate.of(2020, 5, 8).isEqual(DynamicDateUtil.nextOccurrenceByYear(5,2, 5)));
        assertTrue(LocalDate.of(2020, 7, 6).isEqual(DynamicDateUtil.nextOccurrenceByYear(7,1, 1)));
        assertTrue(LocalDate.of(2019, 7, 22).isEqual(DynamicDateUtil.nextOccurrenceByYear(7,4, 1)));
        assertTrue(LocalDate.of(2019, 7, 23).isEqual(DynamicDateUtil.nextOccurrenceByYear(7,4, 2)));
        assertTrue(LocalDate.of(2020, 7, 19).isEqual(DynamicDateUtil.nextOccurrenceByYear(7,3, 0)));
    }
}
