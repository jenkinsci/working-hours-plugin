import { nextOccurrenceChineseLunar } from "../utils/date";
import moment from "moment";

expect.extend({
  toBeSameDay(received, targetDay) {
    const pass = received.isSame(targetDay,'day');
    if (pass) {
      return {
        message: () =>
          `expected ${received} not to be the same day as ${targetDay}`,
        pass: true,
      };
    } else {
      return {
        message: () =>
          `expected ${received} to be the same day as ${targetDay}`,
        pass: false,
      };
    }
  },
});

test("test day to chinese lunar", () => {
  expect(nextOccurrenceChineseLunar('5,6')).toBeSameDay(moment());
});