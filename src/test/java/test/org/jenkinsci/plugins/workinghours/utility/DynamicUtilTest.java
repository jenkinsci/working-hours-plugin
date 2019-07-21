package test.org.jenkinsci.plugins.workinghours.utility;

import org.jenkinsci.plugins.workinghours.model.DynamicDateUtil;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public class DynamicUtilTest {
    @Test
    public void testNextOccurrenceByMonth() {
        LocalDate nextOccurrence = DynamicDateUtil.nextOccurrenceByMonth(1, 1);
        assertTrue(LocalDate.of(2019, 8, 4).isEqual(nextOccurrence));
    }
}
