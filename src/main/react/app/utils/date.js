import moment from "moment";

/**
 * Get next occurrence yearly for a given rule like the first sunday of May.
 *
 * @param month The month for the rule.
 * @param week The nth week of the rule.
 * @param day The weekday of the rule.
 * @param now Today.
 * @returns {moment.Moment} The occurrence in moment of the target date.
 */
export function nextOccurrenceByYear(month, week, day, now) {
  let next = now.clone() || moment();
  let today = now.clone() || moment();
  let nextOccurrenceInThisYear = now.clone() || moment().date(1);
  if (today.month() === month - 1) {
    let tempWeek = week;
    if (nextOccurrenceInThisYear.day() <= day) {
      tempWeek = week - 1;
    }
    nextOccurrenceInThisYear.date(1 + (tempWeek * 7) + (day - nextOccurrenceInThisYear.day()));
    //If in same month but earlier
    if (nextOccurrenceInThisYear.isSameOrAfter(today)) {
      //Add to next year
      return nextOccurrenceInThisYear;
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
 * @param weekOfMonth The nth week of the rule.
 * @param dayOfWeek The weekday of the rule.
 * @param now Today.
 * @returns {moment.Moment} The occurrence in moment of the target date.
 */
export function nextOccurrenceByMonth(weekOfMonth, dayOfWeek, now) {
  let next = now.clone() || moment();
  let today = now.clone() || moment();
  let nextOccurrenceInThisMonth = now.clone() || moment().date(1);
  let tempWeek = weekOfMonth;
  if (nextOccurrenceInThisMonth.day() <= dayOfWeek) {
    tempWeek = weekOfMonth - 1;
  }
  nextOccurrenceInThisMonth.date(1 + (tempWeek * 7) + (dayOfWeek - nextOccurrenceInThisMonth.day()));
  //If in same month but earlier
  if (nextOccurrenceInThisMonth.isSame(today, 'date') ||
    nextOccurrenceInThisMonth.isAfter(today)) {

    return nextOccurrenceInThisMonth;
  }

  //Add to next month
  next.add(1, "month");

  // In earlier month
  next.date(1);
  if (next.day() <= dayOfWeek) {
    weekOfMonth = weekOfMonth - 1;
  }
  next.date(1 + (weekOfMonth * 7) + (dayOfWeek - next.day()));
  return next;
}

export function format(moment) {
  return moment.format("dddd, MMMM Do YYYY");
}

export function formatDate(date) {
  return moment(date).format("dddd, MMMM Do YYYY");
}
