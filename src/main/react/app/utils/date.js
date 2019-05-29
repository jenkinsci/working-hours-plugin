import moment from 'moment';

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
      next.add(1, 'year');
    }
    // In earlier month
  } else if (today.month() > month - 1) {
    next.add(1, 'year');
  }
  next.month(month - 1).date(1);
  if (next.day() <= day) {
    week = week - 1;
  }
  next.date(1 + (week * 7) + (day - next.day()));
  return next;
}

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
    next.add(1, 'month');
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
  return moment.format('dddd, MMMM Do YYYY');
}

export function formatDate(date) {
  if (typeof date === 'string') {
    return date;
  } else {
    return moment(date).format('dddd, MMMM Do YYYY');
  }
}

