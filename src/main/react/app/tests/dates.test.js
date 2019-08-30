import {nextOccurrenceByMonth, nextOccurrenceByYear} from "../utils/date";
import moment from "moment";

expect.extend({
  toBeSameDay(received, targetDay) {
    const pass = received.isSame(targetDay, 'day');
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

test("test next occurrence by month", () => {
  let now = moment().year(2019).month(7 - 1).date(22);
  expect(moment().year(2019).month(8 - 1).date(5)).toBeSameDay(nextOccurrenceByMonth(1, 1, now));
  expect(moment().year(2019).month(7 - 1).date(29)).toBeSameDay(nextOccurrenceByMonth(5, 1, now));
  expect(moment().year(2019).month(8 - 1).date(2)).toBeSameDay(nextOccurrenceByMonth(1, 5, now));
  expect(moment().year(2019).month(8 - 1).date(21)).toBeSameDay(nextOccurrenceByMonth(3, 3, now));
});

test("test next occurrence by year", () => {
  let now = moment().year(2019).month(7 - 1).date(22);
  expect(moment().year(2020).month(1 - 1).date(6)).toBeSameDay(nextOccurrenceByYear(1, 1, 1, now));
  expect(moment().year(2019).month(11 - 1).date(20)).toBeSameDay(nextOccurrenceByYear(11, 3, 3, now));
  expect(moment().year(2020).month(5 - 1).date(8)).toBeSameDay(nextOccurrenceByYear(5, 2, 5, now));
  expect(moment().year(2020).month(7 - 1).date(6)).toBeSameDay(nextOccurrenceByYear(7, 1, 1, now));
  expect(moment().year(2019).month(7 - 1).date(22)).toBeSameDay(nextOccurrenceByYear(7, 4, 1, now));
  expect(moment().year(2019).month(7 - 1).date(23)).toBeSameDay(nextOccurrenceByYear(7, 4, 2, now));
  expect(moment().year(2020).month(7 - 1).date(19)).toBeSameDay(nextOccurrenceByYear(7, 3, 0, now));
});

test("test text format", () => {
  let now = moment().year(2019).month(7 - 1).date(22);
  expect(now.format("dddd, MMMM Do YYYY")).toBe('Monday, July 22nd 2019')
});
