package test.org.jenkinsci.plugins.workinghours.utility;

import net.sf.json.JSONObject;
import org.jenkinsci.plugins.workinghours.model.Holiday;
import org.jenkinsci.plugins.workinghours.presets.PresetManager;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;


public class ChineseLunarCalendarTest {

    @Test
    public void testLunarToSolar() {
        List<Holiday> holidays = PresetManager.getInstance().getRegionHolidays("CN");
        for (Holiday holiday : holidays) {
            System.out.println(JSONObject.fromObject(holiday));
        }
        assertTrue(true);
    }
}
