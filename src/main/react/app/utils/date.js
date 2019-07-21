import moment from "moment";
import ChineseLunarCalendar from "./chineseLunar";

/**
 * Get next occurrence yearly for a given rule like the first sunday of May.
 *
 * @param month The month for the rule.
 * @param week The nth week of the rule.
 * @param day The weekday of the rule.
 * @returns {moment.Moment} The occurrence in moment of the target date.
 */
export function nextOccurrenceByYear(month, week, day) {
  let next = moment().hour(0).second(0).minute(0);
  let today = moment().hour(0).second(0).minute(0);
  let nextOccurrenceInThisMonth = moment().hour(0).second(0).minute(0).date(1);
  if (today.month() === month - 1) {
    let tempWeek = week;
    if (nextOccurrenceInThisMonth.day() <= day) {
      tempWeek = week - 1;
    }
    nextOccurrenceInThisMonth.date(1 + (tempWeek * 7) + (day - nextOccurrenceInThisMonth.day()));
    //If in same month but earlier
    if (nextOccurrenceInThisMonth.isSameOrAfter(today)) {
      //Add to next year
      return nextOccurrenceInThisMonth;
    } else {
      next.add(1, "year");
    }
    // In earlier month
  } else if (today.month() > month - 1) {
    next.add(1, "year");
  }
  next.month(month - 1).date(1);
  if (next.day() <= day) {
    week = week - 1;
  }
  next.date(1 + (week * 7) + (day - next.day()));
  return next;
}

/**
 * Get next occurrence each month for a given rule like the first sunday.
 *
 * @param week The nth week of the rule.
 * @param day The weekday of the rule.
 * @returns {moment.Moment} The occurrence in moment of the target date.
 */
export function nextOccurrenceByMonth(week, day) {
  let next = moment().hour(0).second(0).minute(0);
  let today = moment().hour(0).second(0).minute(0);
  let nextOccurrenceInThisMonth = moment().hour(0).second(0).minute(0).date(1);
  let tempWeek = week;
  if (nextOccurrenceInThisMonth.day() <= day) {
    tempWeek = week - 1;
  }
  nextOccurrenceInThisMonth.date(1 + (tempWeek * 7) + (day - nextOccurrenceInThisMonth.day()));
  //If in same month but earlier
  if (nextOccurrenceInThisMonth.isSameOrAfter(today)) {
    return nextOccurrenceInThisMonth;
  } else {
    //Add to next month
    next.add(1, "month");
  }
  // In earlier month
  next.date(1);
  if (next.day() <= day) {
    week = week - 1;
  }
  next.date(1 + (week * 7) + (day - next.day()));
  return next;
}

export function format(moment) {
  return moment.format("dddd, MMMM Do YYYY");
}

export function formatDate(date) {
  return moment(date).format("dddd, MMMM Do YYYY");
}

/**
 * Get next occurrence of a Chinese Lunar type date.
 * @param param Params used to decide a regional(At here, it's chinese lunar calendar) date.
 * Typically, param for a chinese lunar day is like "1,1". Which stands for the first day of the first lunar month.
 */
export function nextOccurrenceChineseLunar(param) {
  let splitParam = param.split(",").map(item => parseInt(item));
  if (splitParam[0] && splitParam[1]) {
    let now = moment();

    /*This year's target day in solar calendar.*/
    let solarResult = ChineseLunarCalendar.lunar2solar(now.year(), splitParam[0], splitParam[1]);

    if (moment({
      year: solarResult.cYear,
      month: solarResult.cMonth - 1,
      date: solarResult.cDay
    }).isBefore(moment({ year: now.year(), month: now.month(), date: now.date() }))) {
      /*If is before today, we set it to the next year.*/
      let solarResultNextYear = ChineseLunarCalendar.lunar2solar(now.year() + 1, splitParam[0], splitParam[1]);
      console.log(solarResultNextYear);
      return moment({
        year: solarResultNextYear.cYear,
        month: solarResultNextYear.cMonth - 1,
        date: solarResultNextYear.cDay
      });
    } else {
      return moment({
        year: solarResult.cYear,
        month: solarResult.cMonth - 1,
        date: solarResult.cDay
      });
    }
  }
}
